package com.ulegalize.lawfirm.rest.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.slack.Attachment;
import com.ulegalize.dto.slack.Fields;
import com.ulegalize.dto.slack.SlackMessage;
import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
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
public class SlackApiImpl implements SlackApi {
    @Value("${app.v1.slack.webhook}")
    String SLACK_INFO_URL;
    @Value("${app.slack.sensitive.webhook}")
    String SLACK_URL;
    @Value("${app.slack.newArrival.webhook}")
    String SLACK_NEW_ARRIVAL_URL;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private RestTemplate restTemplate;

    public SlackApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public <T> void sendSensitiveNotification(String method, T info, EnumSlackUrl enumSlackUrl) throws ResponseStatusException {
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")
                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("test")
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
                    .text(enumSlackUrl.getDescription())
                    .icon_emoji(":twice:")
                    .attachments(Collections.singletonList(attachment))
                    .build();
            sendNotification(slackMessage, enumSlackUrl);
        }
    }

    private void sendNotification(SlackMessage message, EnumSlackUrl enumSlackUrl) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            log.debug("sendNotification json {}", json);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            String url = null;
            switch (enumSlackUrl) {
                case SENSITIVE:
                    url = SLACK_URL;
                    break;
                case INFO:
                case CALENDAR_SCHEDULER:
                    url = SLACK_INFO_URL;
                    break;
                case NEW_ARRIVAL:
                    url = SLACK_NEW_ARRIVAL_URL;
                    break;
            }

            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        } catch (IOException e) {
            log.error("error while contacting slack");
        }
    }
}