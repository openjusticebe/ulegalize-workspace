package com.ulegalize.lawfirm.kafka.producer.transparency.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class CaseProducerImpl implements ICaseProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.createCase-topic-name}")
    private String topicName;
    @Value("${tpd.createLawfirm-topic-name}")
    private String topicLawfirmName;
    @Value("${tpd.createShareCases-topic-name}")
    private String topicCreateShareCase;

    public CaseProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createCaseMessage(CaseCreationDTO message, LawfirmToken lawfirmToken) {
        log.debug("Entering createCaseMessage with payload : caseCreationDTO {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {

                KafkaObject<CaseCreationDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);
                kafkaTemplate.send(topicName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createCaseMessage ", e);
            }
        }
    }

    @Override
    public void createLawfirmMessage(LawfirmToken lawfirmToken, String newVcKey, String userEmail, String language, long userId) {
        log.debug("Entering createLawfirmMessage lawfirmToken {}, newVcKey {} and userEmail {}", lawfirmToken, newVcKey, userEmail);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                ProfileDTO profileDTO = new ProfileDTO();

                profileDTO.setEmail(userEmail);
                profileDTO.setVcKeySelected(newVcKey);
                profileDTO.setLanguage(language);
                profileDTO.setUserId(userId);
                log.debug("Payload : {}", profileDTO);

                KafkaObject<ProfileDTO> messageKafka = new KafkaObject<>(lawfirmToken, profileDTO);
                kafkaTemplate.send(topicLawfirmName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createLawfirmMessage ", e);
            }
        }
    }

    @Override
    public void createShareCases(LawfirmToken lawfirmToken, UpdateShareRequestDTO message) throws ResponseStatusException {
        log.debug("Entering createShareCases updateShareRequest {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<UpdateShareRequestDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);
                kafkaTemplate.send(topicCreateShareCase, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createShareCases ", e);
            }
        }
    }
}