package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TVirtualcabConfigPk implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vcKey;
    private String parameter;
    private String description;

}