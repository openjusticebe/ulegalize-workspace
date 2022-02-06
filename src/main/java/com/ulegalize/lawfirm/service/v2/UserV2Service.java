package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.entity.TUsers;

public interface UserV2Service {
    void changeLanguage(Long userId, String language);

    TUsers createUsers(String userEmail, String clientFrom, EnumLanguage language);

    void deleteUser(Long userId);

    boolean verifyUser(String email, String hashkey);
}
