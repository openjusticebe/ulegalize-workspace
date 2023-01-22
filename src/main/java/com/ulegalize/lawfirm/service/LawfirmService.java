package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmDTO;

import java.util.List;

public interface LawfirmService {
    List<LawfirmDTO> getLawfirmList(String searchCriteria);

}
