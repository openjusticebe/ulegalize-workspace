package com.ulegalize.lawfirm.kafka.producer.drive.impl;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.security.UlegalizeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component(value = "driveProducerDropBox")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class DriveProducerDropBoxImpl implements IDriveProducer {

    @Value("${app.lawfirm-drive.url}")
    String DRIVE_DROPBOX_URL;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final RestTemplate restTemplate;

    public DriveProducerDropBoxImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public void createFolders(UlegalizeToken ulegalizeToken, String vcKey, List<String> paths) {
        // TODO must be implemented with kafka
        log.info("Entering createFolder with container : {} and paths {}", vcKey, paths);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + ulegalizeToken.getToken());

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("paths", paths);

            HttpEntity<List> request = new HttpEntity<>(paths, headers);
            ResponseEntity<String> result = restTemplate.exchange(DRIVE_DROPBOX_URL + "v2/dropbox/folders", HttpMethod.POST, request, String.class);
        }
        log.info("Leaving createFolder");
    }

    @Override
    public void createContainer(UlegalizeToken ulegalizeToken, String containerName, List<String> paths) throws LawfirmBusinessException {
        log.error("Don't create a container {} for dropbox", containerName);
        throw new LawfirmBusinessException("Don't create a container " + containerName + " for dropbox");

    }
}