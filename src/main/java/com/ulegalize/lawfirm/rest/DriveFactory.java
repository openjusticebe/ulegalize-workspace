package com.ulegalize.lawfirm.rest;

import com.ulegalize.enumeration.DriveType;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DriveFactory {
    @Autowired
    @Qualifier("driveOpenStack")
    private DriveApi driveOpenStack;
    @Autowired
    @Qualifier("driveDropbox")
    private DriveApi driveDropbox;
    @Autowired
    @Qualifier("driveOnedrive")
    private DriveApi driveOnedrive;

    @Autowired
    @Qualifier("driveProducer")
    private IDriveProducer driveProducer;

    @Autowired
    @Qualifier("driveProducerDropBox")
    private IDriveProducer driveProducerDropBox;
    @Autowired
    @Qualifier("driveProducerOnedrive")
    private IDriveProducer driveProducerOnedrive;

    public DriveApi getDriveImpl(DriveType driveType) {
        log.debug("Drive choice {}", driveType);
        return switch (driveType) {
            case openstack -> driveOpenStack;
            case dropbox -> driveDropbox;
            case onedrive -> driveOnedrive;
            default -> driveOpenStack;
        };
    }

    public IDriveProducer getDriveProducer(DriveType driveType) {
        log.debug("Drive producer choice {}", driveType);
        return switch (driveType) {
            case openstack -> driveProducer;
            case dropbox -> driveProducerDropBox;
            case onedrive -> driveProducerOnedrive;
            default -> driveProducer;
        };
    }
}
