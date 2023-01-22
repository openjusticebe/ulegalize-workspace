package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.model.entity.TMessage;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class DTOToMessageEntityConverter implements SuperTriConverter<MessageDTO, TMessage, TMessage> {


    @Override
    public TMessage apply(MessageDTO messageDTO, TMessage tMessage) {

        tMessage.setMessageFr(StringEscapeUtils.escapeHtml4(messageDTO.getMessageFr()));
        tMessage.setMessageEn(StringEscapeUtils.escapeHtml4(messageDTO.getMessageEn()));
        tMessage.setMessageNl(StringEscapeUtils.escapeHtml4(messageDTO.getMessageNl()));
        tMessage.setMessageDe(StringEscapeUtils.escapeHtml4(messageDTO.getMessageDe()));

        return tMessage;
    }
}
