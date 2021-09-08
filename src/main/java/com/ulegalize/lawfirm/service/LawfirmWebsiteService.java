package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmWebsiteDTO;

public interface LawfirmWebsiteService {

    LawfirmWebsiteDTO getLawfirmWebsites(String vcKey);

    LawfirmWebsiteDTO updateLawfirmWebsite(String vcKey, LawfirmWebsiteDTO lawfirmWebsiteDTO);
}
