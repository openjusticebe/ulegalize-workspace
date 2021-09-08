package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;

import java.util.List;

public interface LawyerService {
    List<LawyerDTO> getFilterLawyer(String search, String name, String pref) throws LawfirmBusinessException;

    LawyerDTO getPublicLawyer(String aliasPublic) throws LawfirmBusinessException;

    LawyerDTO getLayerByEmail(String userEmail) throws LawfirmBusinessException;
}
