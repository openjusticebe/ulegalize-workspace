package com.ulegalize.lawfirm.rest.impl.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Data
public class SlackMessage implements Serializable {

    private String username;
    private String text;
    private String icon_emoji;
    private List<Attachment> attachments;
}