package com.ulegalize.lawfirm.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"dev", "devDocker", "test", "prod"})
public class KafkaConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${tpd.createCase-topic-name}")
    private String topicName;
    @Value("${tpd.createLawfirm-topic-name}")
    private String topicLawfirmName;
    @Value("${tpd.createShareCases-topic-name}")
    private String topicCreateShareCase;
    @Value("${tpd.shareUserDossier-topic-name}")
    private String topicShareUserDossier;
    @Value("${tpd.attachAffaire-topic-name}")
    private String topicAttachAffaire;
    @Value("${tpd.sendMail-topic-name}")
    private String sendMailTopic;
    @Value("${tpd.updateLawfirm-topic-name}")
    private String topicUpdateLawfirm;
    @Value("${tpd.switchLawfirm-topic-name}")
    private String topicSwitchLawfirm;
    @Value("${tpd.updateLawfirmNotification-topic-name}")
    private String updateNotificationLawfirmTopic;
    @Value("${tpd.createInvoiceRecord-topic-name}")
    private String createInvoiceRecordTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildAdminProperties());
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(props);
    }

    //
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic adviceTopic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic lawfirmTopic() {
        return TopicBuilder.name(topicLawfirmName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createShareCaseTopic() {
        return TopicBuilder.name(topicCreateShareCase)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shareUserDossierTopic() {
        return TopicBuilder.name(topicShareUserDossier)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic attachAffaireTopic() {
        return TopicBuilder.name(topicAttachAffaire)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sendMailTopic() {
        return TopicBuilder.name(sendMailTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createUpdateLawfirmTopic() {
        return TopicBuilder.name(topicUpdateLawfirm)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createSwitchLawfirmTopic() {
        return TopicBuilder.name(topicSwitchLawfirm)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic updateNotificationLawfirmTopic() {
        return TopicBuilder.name(updateNotificationLawfirmTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createInvoiceRecordTopic() {
        return TopicBuilder.name(createInvoiceRecordTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
