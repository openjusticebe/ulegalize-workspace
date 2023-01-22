package com.ulegalize.lawfirm.controller;

import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.service.impl.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/message")
@Slf4j
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @GetMapping()
    public MessageDTO getMessage() {
        log.debug("getMessages() from messageController");

        return messageService.getMessage();
    }

    @PutMapping(value = "/deactivate")
    public void deactivateMessage() {
        log.debug("deactivateMessage");

        messageService.deactivateMessage();
    }
}
