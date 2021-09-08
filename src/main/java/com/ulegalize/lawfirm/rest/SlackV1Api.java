package com.ulegalize.lawfirm.rest;

import org.springframework.web.server.ResponseStatusException;

public interface SlackV1Api {
    public <T> void sendSensitiveNotification(String method, T info) throws ResponseStatusException;
}
