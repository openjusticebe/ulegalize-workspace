package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TDelegatePk implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idUser;

    private String vcKey = "";

}