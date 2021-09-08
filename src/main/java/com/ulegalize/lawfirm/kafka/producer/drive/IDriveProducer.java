package com.ulegalize.lawfirm.kafka.producer.drive;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.security.UlegalizeToken;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface IDriveProducer {

    void createFolders(UlegalizeToken ulegalizeToken, String vcKey, List<String> paths);

    public void createContainer(UlegalizeToken ulegalizeToken, String containerName, List<String> paths) throws ResponseStatusException, LawfirmBusinessException;

}