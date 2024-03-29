package com.ulegalize.lawfirm.kafka.producer.transparency.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.lawfirm.kafka.producer.transparency.IAffaireProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class AffaireProducerImpl implements IAffaireProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.attachAffaire-topic-name}")
    private String topicName;
    @Value("${tpd.updateAffaire-topic-name}")
    private String updateAffaireTopic;

    public AffaireProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void attachAffaire(CaseCreationDTO message, LawfirmToken lawfirmToken) {
        log.debug("Entering attachAffaire with payload : caseCreationDTO {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<CaseCreationDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);
                kafkaTemplate.send(topicName, messageKafka);

                log.info("All messages received attachAffaire");
            } catch (Exception e) {
                log.error("Error while createCaseMessage ", e);
            }
        }
    }

    @Override
    public void updateAffaire(CaseCreationDTO message, LawfirmToken lawfirmToken) {
        log.debug("Entering updateAffaire with payload : caseCreationDTO {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<CaseCreationDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);
                kafkaTemplate.send(updateAffaireTopic, messageKafka);

                log.info("All messages received updateAffaire");
            } catch (Exception e) {
                log.error("Error while updateAffaire ", e);
            }
        }
    }
}