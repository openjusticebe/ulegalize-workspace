package com.ulegalize.lawfirm.rest.impl;

import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.model.dto.Auth0User;
import com.ulegalize.lawfirm.model.entity.Auth0Entity;
import com.ulegalize.lawfirm.repository.Auth0Repository;
import com.ulegalize.lawfirm.rest.AuthApi;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AuthApiImpl implements AuthApi {
    @Value("${app.auth0.domain}")
    String URL_AUTH0_API;

    @Value("${app.auth0.clientId}")
    String CLIENT_ID;
    @Value("${app.auth0.clientSecret}")
    String CLIENT_SECRET;
    @Value("${app.auth0.audience}")
    String AUDIENCE;
    private final RestTemplate restTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    private final Auth0Repository auth0Repository;

    public AuthApiImpl(RestTemplate restTemplate, Auth0Repository auth0Repository) {
        this.restTemplate = restTemplate;
        this.auth0Repository = auth0Repository;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public void updateAppMetaData(String user_id, String vcKey) throws ResponseStatusException {
        log.debug("Entering updateAppMetaData with user id : {} and  vcKey {}", user_id, vcKey);
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {
            if (vcKey != null) {
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("Authorization", "Bearer " + getToken());

                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonAppMetaData = new JSONObject();
                    jsonAppMetaData.put("vcKey", vcKey);
                    jsonObject.put("app_metadata", jsonAppMetaData);
                    HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);
                    ResponseEntity<String> response = restTemplate
                            .exchange(URL_AUTH0_API + "/api/v2/users/" + user_id, HttpMethod.PATCH, request, String.class);

                    log.debug("response {}", response.getStatusCodeValue());
                } catch (ResponseStatusException re) {
                    log.error("Error while getting token from auth0");
                    Optional<Auth0Entity> optionalAuth0Entity = auth0Repository.findFirstBy();
                    if (optionalAuth0Entity.isPresent()) {
                        // force new call into getToken
                        // minus 1 day
                        optionalAuth0Entity.get().setExpireIn(-86000);
                        auth0Repository.save(optionalAuth0Entity.get());

                    }
                    // try another time to have a last chance with error from auth0 (token expire or token issue 401)
                    updateAppMetaData(user_id, vcKey);
                }
            }
        }
    }

    @Override
    public void changePassword(String user_id, String newPwd) throws ResponseStatusException {
        log.debug("Entering changePassword with payload : {}", user_id);
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + getToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", newPwd);
            jsonObject.put("connection", "Username-Password-Authentication");
            HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);
            ResponseEntity<String> response = restTemplate
                    .exchange(URL_AUTH0_API + "/api/v2/users/" + user_id, HttpMethod.PATCH, request, String.class);

            log.debug("response {}", response.getStatusCodeValue());
        }
    }

    @Override
    public List<Auth0User> getActiveUsers() throws ResponseStatusException {
        log.debug("Entering getActiveUsers");
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + getToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("connection", "Username-Password-Authentication");
            HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);
//        last_login:[2022-06-25 TO 2022-06-26]
            LocalDate localDate = LocalDate.now().minusWeeks(1);
            ResponseEntity<Auth0User[]> response = restTemplate.exchange(URL_AUTH0_API + "/api/v2/users" + "?q=last_login:[" + localDate + " TO " + LocalDate.now() + "]&search_engine=v3", HttpMethod.GET, request, Auth0User[].class);

            if (response.getBody() != null) {
                return List.of(response.getBody());
            }
        }
        return null;
    }

    private String getToken() throws ResponseStatusException {
        log.debug("Entering getToken");

        Optional<Auth0Entity> optionalAuth0Entity = auth0Repository.findFirstBy();
        if (optionalAuth0Entity.isPresent()) {
            ZoneId zoneIdGmt = ZoneId.of("Etc/UTC");
            ZonedDateTime utimeDate = optionalAuth0Entity.get().getCreDate().withZoneSameInstant(zoneIdGmt).plus(optionalAuth0Entity.get().getExpireIn(), ChronoUnit.SECONDS);
            log.debug("utimeDate {}", utimeDate);
            log.debug("DB create date {}", optionalAuth0Entity.get().getCreDate());
            log.debug("DB exp in {}", optionalAuth0Entity.get().getExpireIn());

            if (utimeDate.isAfter(ZonedDateTime.now(zoneIdGmt))) {
                log.info("ultimate is AFTER now {}", LocalDateTime.now());
                return optionalAuth0Entity.get().getAccessToken();
            } else {
                log.info("ultimate is NOT AFTER now {}", LocalDateTime.now());
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("grant_type", "client_credentials");
        jsonObject.put("client_id", CLIENT_ID);
        jsonObject.put("client_secret", CLIENT_SECRET);
        jsonObject.put("audience", AUDIENCE);
        HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);

        ResponseEntity<JSONObject> response = restTemplate
                .exchange(URL_AUTH0_API + "/oauth/token", HttpMethod.POST, request, JSONObject.class);

        log.debug("token {}", response.getBody().getAsString("access_token"));
        Auth0Entity auth0Entity = optionalAuth0Entity.orElse(new Auth0Entity());
        auth0Entity.setAccessToken(response.getBody().getAsString("access_token"));
        auth0Entity.setExpireIn((Integer) response.getBody().getAsNumber("expires_in"));

        auth0Repository.save(auth0Entity);

        return response.getBody().getAsString("access_token");


    }
}