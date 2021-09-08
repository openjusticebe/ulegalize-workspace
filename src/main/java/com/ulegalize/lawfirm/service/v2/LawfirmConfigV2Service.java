package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.LawfirmConfigDTO;

import java.util.List;

public interface LawfirmConfigV2Service {
    List<LawfirmConfigDTO> getLawfirmConfigInfoByVcKey(String vcKey);

    void addLawfirmConfigByVcKey(LawfirmConfigDTO lawfirmConfigDTO, String vcKey);

    void removeLawfirmConfig(String lawfirmConfigDescription, String vcKey);
}
