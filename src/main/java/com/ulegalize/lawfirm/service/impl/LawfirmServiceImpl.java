package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.service.LawfirmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class LawfirmServiceImpl implements LawfirmService {

    private final LawfirmRepository lawfirmRepository;

    public LawfirmServiceImpl(LawfirmRepository lawfirmRepository) {
        this.lawfirmRepository = lawfirmRepository;
    }


    @Override
    public LawfirmEntity updateToken(LawfirmEntity lawfirm, LawfirmEntity lawfirmUpdted) throws LawfirmBusinessException {
        log.debug("Entering updateToken with driveType {}, expire {}, onedrive {} , dropbox {}", lawfirm.getDriveType(), lawfirm.getExpireToken(), lawfirm.getOnedriveToken(), lawfirm.getDropboxToken());

        if (lawfirmUpdted.getDriveType() == null) {
            throw new LawfirmBusinessException("Drive type must be specified");
        }

        lawfirm.setDriveType(lawfirmUpdted.getDriveType());
        lawfirm.setExpireToken(LocalDateTime.now());

        switch (lawfirmUpdted.getDriveType()) {
            case dropbox:
                lawfirm.setDropboxToken(lawfirmUpdted.getDropboxToken());
                lawfirm.setExpireToken(lawfirmUpdted.getExpireToken());

                break;
            case onedrive:
                lawfirm.setOnedriveToken(lawfirmUpdted.getOnedriveToken());
                lawfirm.setRefreshToken(lawfirmUpdted.getRefreshToken());
                break;
            case openstack:
                break;
            default:
        }

        lawfirmRepository.save(lawfirm);
        log.debug("lawfirm saved successfully");
        return lawfirm;
    }

}
