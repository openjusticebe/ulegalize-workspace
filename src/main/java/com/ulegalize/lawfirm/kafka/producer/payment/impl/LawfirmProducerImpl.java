package com.ulegalize.lawfirm.kafka.producer.payment.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.kafka.producer.payment.ILawfirmProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class LawfirmProducerImpl implements ILawfirmProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.updateLawfirm-topic-name}")
    private String topicName;
    @Value("${tpd.updateLawfirmNotification-topic-name}")
    private String updateNotificationTopicName;
    @Value("${tpd.switchLawfirm-topic-name}")
    private String topicSwitchLawfirm;

    public LawfirmProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updateLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken) {
        log.debug("Entering producer updateLawfirm lawfirm dto {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<LawfirmDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);

                ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName,  messageKafka);

                future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("failure");
                    }

                    @Override
                    public void onSuccess(SendResult<String, Object> sendResult) {
                        log.info("Kafka sent message with success='{}', sendResult='{}'", message, sendResult);
                    }

                });
                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while updateLawfirm ", e);
            }
        }
    }
    @Override
    public void updateNotificationLawfirm(LawfirmDTO message, LawfirmToken LawfirmToken){
        log.debug("Entering producer updateNotificationLawfirm lawfirm dto {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<LawfirmDTO> messageKafka = new KafkaObject<>(LawfirmToken, message);

                ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(updateNotificationTopicName,  messageKafka);

                future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("Kafka unable to sent message='{}'", message, ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, Object> sendResult) {
                        log.info("Kafka sent message with success='{}', sendResult='{}'", message, sendResult);
                    }

                });
                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while updateNotificationLawfirm ", e);
            }
        }
    }

    @Override
    public void switchLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken) {
        log.debug("Entering producer switchLawfirm lawfirm dto {}", message);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<LawfirmDTO> messageKafka = new KafkaObject<>(lawfirmToken, message);

                ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicSwitchLawfirm, messageKafka);

                future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("Kafka unable to sent message='{}'", message, ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, Object> sendResult) {
                        log.info("Kafka sent message with success='{}'", message);
                    }

                });
                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while switchLawfirm ", e);
            }
        }
    }
}