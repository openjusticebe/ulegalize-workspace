package com.ulegalize.lawfirm.rest.impl;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.rest.LawfirmTransparencyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class LawfirmTransparencyImpl implements LawfirmTransparencyApi {
    @Value("${app.lawfirm-transparency.url}")
    String URL_TRANSPARENCY_API;

    private RestTemplate restTemplate;

    public LawfirmTransparencyImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public void createCasFromAgenda(LawfirmCalendarEventDTO lawfirmCalendarEventDTO, String token) throws ResponseStatusException {
        log.debug("Entering createCasLawfirm with payload : {}", lawfirmCalendarEventDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-access-token", token);

        HttpEntity<LawfirmCalendarEventDTO> request = new HttpEntity<>(lawfirmCalendarEventDTO, headers);
        ResponseEntity<Void> response = restTemplate
                .exchange(URL_TRANSPARENCY_API + "v1/cases/agenda", HttpMethod.POST, request, Void.class);

    }

    @Override
    public void createShareCases(String internalToken, UpdateShareRequestDTO updateShareRequest) throws ResponseStatusException {
        log.debug("Entering createShareCases with payload : {}", updateShareRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-access-token", internalToken);

        HttpEntity<UpdateShareRequestDTO> request = new HttpEntity<>(updateShareRequest, headers);
        ResponseEntity<Void> response = restTemplate
                .exchange(URL_TRANSPARENCY_API + "v1/cases/share", HttpMethod.POST, request, Void.class);

    }
}