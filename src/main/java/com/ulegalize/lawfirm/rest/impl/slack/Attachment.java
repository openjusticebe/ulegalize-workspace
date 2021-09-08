package com.ulegalize.lawfirm.rest.impl.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Data
public class Attachment implements Serializable {

    private String color;
    private List<Fields> fields;
}