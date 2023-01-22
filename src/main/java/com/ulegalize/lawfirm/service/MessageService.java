package com.ulegalize.lawfirm.service;

import com.ulegalize.lawfirm.model.dto.MessageDTO;

public interface MessageService {


    MessageDTO getMessage();

    void deactivateMessage();

    Long createMessage(MessageDTO messageDTO);
}
