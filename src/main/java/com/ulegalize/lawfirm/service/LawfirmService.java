package com.ulegalize.lawfirm.service;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;

public interface LawfirmService {
    public LawfirmEntity updateToken(LawfirmEntity lawfirm, LawfirmEntity lawfirmUpdted) throws LawfirmBusinessException;

}
