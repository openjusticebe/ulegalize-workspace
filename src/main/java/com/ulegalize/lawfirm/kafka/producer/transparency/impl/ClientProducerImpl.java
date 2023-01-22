package com.ulegalize.lawfirm.kafka.producer.transparency.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.kafka.producer.transparency.IClientProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class ClientProducerImpl implements IClientProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${tpd.updateClient-topic-name}")
    private String updateClientTopic;

    public ClientProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updateClient(ContactSummary message, LawfirmToken lawfirmToken) {
        log.debug("Entering updateClient with payload : contactSummary {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<ContactSummary> messageKafka = new KafkaObject<>(lawfirmToken, message);
                kafkaTemplate.send(updateClientTopic, messageKafka);

                log.info("All messages received updateClient");
            } catch (Exception e) {
                log.error("Error while updateClient ", e);
            }
        }
    }
}
