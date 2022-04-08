package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;

import java.util.List;

public interface LawfirmService {
    public List<LawfirmDTO> getLawfirmList();

    public LawfirmEntity updateToken(LawfirmEntity lawfirm, LawfirmEntity lawfirmUpdted) throws LawfirmBusinessException;

}
