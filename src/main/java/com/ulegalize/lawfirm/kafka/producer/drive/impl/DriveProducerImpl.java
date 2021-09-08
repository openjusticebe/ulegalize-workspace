package com.ulegalize.lawfirm.kafka.producer.drive.impl;

import com.ulegalize.KafkaObject;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.security.UlegalizeToken;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component(value = "driveProducer")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class DriveProducerImpl implements IDriveProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.createFolder-topic-name}")
    private String topicName;
    @Value("${tpd.createContainer-topic-name}")
    private String topicContainerName;

    public DriveProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createFolders(UlegalizeToken ulegalizeToken, String vcKey, List<String> paths) {
        log.debug("Entering createFolders vcKey {} and path {}", vcKey, paths);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("container", vcKey);
                jsonObject.put("paths", paths);

                KafkaObject<JSONObject> messageKafka = new KafkaObject<>(ulegalizeToken, jsonObject);
                kafkaTemplate.send(topicName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createFolders ", e);
            }
        }
    }

    @Override
    public void createContainer(UlegalizeToken ulegalizeToken, String containerName, List<String> paths) throws ResponseStatusException {
        log.debug("Entering createContainer vcKey {} and path {}", containerName, paths);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("test")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("container", containerName);
                jsonObject.put("paths", paths);

                KafkaObject<JSONObject> messageKafka = new KafkaObject<>(ulegalizeToken, jsonObject);
                kafkaTemplate.send(topicContainerName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createContainer ", e);
            }
        }
    }
}