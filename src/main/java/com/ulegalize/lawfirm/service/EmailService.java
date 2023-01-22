package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.lawfirm.model.entity.EmailsEntity;

public interface EmailService {
    EmailsEntity registeredUser(String vcKey, ProfileDTO userProfile) throws RuntimeException;

    void reminderSignup() throws RuntimeException;
}
