package com.ulegalize.lawfirm.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.rest.SlackV1Api;
import com.ulegalize.lawfirm.rest.impl.slack.Attachment;
import com.ulegalize.lawfirm.rest.impl.slack.Fields;
import com.ulegalize.lawfirm.rest.impl.slack.SlackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SlackV1ApiImpl implements SlackV1Api {
    @Value("${app.v1.slack.webhook}")
    String SLACK_URL;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private RestTemplate restTemplate;

    public SlackV1ApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public <T> void sendSensitiveNotification(String method, T info) throws ResponseStatusException {
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
        ) {

            List<Fields> fieldsList = new ArrayList<>();
            Fields fields = Fields.builder()
                    .title("Environment")
                    .value(activeProfile)
                    .build();
            fieldsList.add(fields);

            Fields fields1 = Fields.builder()
                    .title("Method name")
                    .value(method)
                    .build();
            fieldsList.add(fields1);

            Fields fields2 = Fields.builder()
                    .title("Info id")
                    .value(info.toString())
                    .build();
            fieldsList.add(fields2);

            Attachment attachment = Attachment.builder()
                    .color("#461D75")
                    .fields(fieldsList)
                    .build();

            SlackMessage slackMessage = SlackMessage.builder()
                    .username("Ulegalize master")
                    .text("Issue within ulegalize-lawfirm")
                    .icon_emoji(":twice:")
                    .attachments(Collections.singletonList(attachment))
                    .build();
            sendNotification(slackMessage);
        }
    }

    private void sendNotification(SlackMessage message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            log.debug("sendNotification json {}", json);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            restTemplate.exchange(SLACK_URL, HttpMethod.POST, entity, String.class);

        } catch (IOException e) {
            log.error("error while contacting slack");
        }
    }
}