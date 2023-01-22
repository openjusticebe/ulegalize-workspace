package com.ulegalize.lawfirm.kafka.producer.payment.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.lawfirm.kafka.producer.payment.IPaymentProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class PaymentProducerImpl implements IPaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.createInvoiceRecord-topic-name}")
    private String topicCreateInvoiceRecord;

    @Value("${tpd.sendReportTopic-topic-name}")
    private String sendReportTopic;

    public PaymentProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPayment(LawfirmToken lawfirmToken, LawfirmCalendarEventDTO calendarEvent) {
        log.debug("Entering sendPayment vcKey {} and types {}", lawfirmToken.getVcKey(), calendarEvent);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaObject<LawfirmCalendarEventDTO> messageKafka = new KafkaObject<>(lawfirmToken, calendarEvent);
                kafkaTemplate.send(topicCreateInvoiceRecord, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while sendPayment ", e);
            }
        }
    }

    @Override
    public void sendReportTopic(Long totalWorkspace, long totalUser, Long newTotalUserWeek) {
        log.debug("Entering sendReportTopic ");
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("totalWorkspace", totalWorkspace);
                jsonObject.put("totalUser", totalUser);
                jsonObject.put("newTotalUserWeek", newTotalUserWeek);
                KafkaObject<JSONObject> messageKafka = new KafkaObject<>(null, jsonObject);
                kafkaTemplate.send(sendReportTopic, messageKafka);

                log.info("All messages sendReportTopic received");
            } catch (Exception e) {
                log.error("Error while sendReportTopic ", e);
            }
        }
    }
}