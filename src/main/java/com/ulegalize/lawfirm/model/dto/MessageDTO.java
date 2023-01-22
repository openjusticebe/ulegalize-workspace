package com.ulegalize.lawfirm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long userId;
    private boolean isValid;
    private String message;

    private String messageFr;
    private String messageEn;
    private String messageNl;
    private String messageDe;

    private ZonedDateTime dateTo;

    public MessageDTO(Long userId, boolean isValid, String message) {

        this.userId = userId;
        this.isValid = isValid;
        this.message = message;

    }

}
