package com.ulegalize.lawfirm.kafka.producer.template.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.dto.template.InvoiceTemplateDTO;
import com.ulegalize.lawfirm.kafka.producer.template.IInvoiceProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class InvoiceProducerImpl implements IInvoiceProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.createInvoiceTemplateBigleLegal-topic-name}")
    private String createInvoiceTemplateBigleLegal;
    @Value("${biglelegal.activation}")
    private Boolean bigleLegalActivation;

    public InvoiceProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createDocument(LawfirmToken lawfirmToken, InvoiceTemplateDTO invoiceTemplateDTO) {
        log.debug("Entering producer createDocument InvoiceTemplateDTO {}", invoiceTemplateDTO);
        // TODO onlyfor dev change when ==> prod

        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("test")
                && !activeProfile.equalsIgnoreCase("devDocker")
                && bigleLegalActivation) {
            try {
                KafkaObject<InvoiceTemplateDTO> messageKafka = new KafkaObject<>(lawfirmToken, invoiceTemplateDTO);
                kafkaTemplate.send(createInvoiceTemplateBigleLegal, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while sendInvoiceTemplate ", e);
            }
        }
    }
}
