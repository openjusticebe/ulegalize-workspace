package com.ulegalize.lawfirm.rest.v2;

import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import org.springframework.web.server.ResponseStatusException;

public interface SlackApi {
    public <T> void sendSensitiveNotification(String method, T info, EnumSlackUrl enumSlackUrl) throws ResponseStatusException;
}
