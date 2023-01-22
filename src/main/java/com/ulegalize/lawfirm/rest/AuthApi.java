package com.ulegalize.lawfirm.rest;

import com.ulegalize.lawfirm.model.dto.Auth0User;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface AuthApi {
    void updateAppMetaData(String user_id, String vcKey) throws ResponseStatusException;

    void changePassword(String user_id, String newPwd) throws ResponseStatusException;

    List<Auth0User> getActiveUsers() throws ResponseStatusException;

}
