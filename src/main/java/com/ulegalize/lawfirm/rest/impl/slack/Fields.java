package com.ulegalize.lawfirm.rest.impl.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Data
public class Fields implements Serializable {

    private String title;
    private String value;
}