package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.entity.TUsers;

import java.util.List;

public interface UserV2Service {
    void changeLanguage(Long userId, String language);

    TUsers createUsers(String userEmail, String clientFrom, EnumLanguage language);

    void deleteUser(Long userId);

    boolean verifyUser(String email, String hashkey);

    TUsers findById(Long userId);

    List<LawyerDTO> findValid();

    List<LawyerDTO> getLawfirmUsers(String vcKey);

    List<LawfirmUserDTO> geLawfirmUserByVcKey(String vcKey);
}
