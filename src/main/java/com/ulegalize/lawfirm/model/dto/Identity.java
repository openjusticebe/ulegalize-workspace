
package com.ulegalize.lawfirm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Identity {


    private String connection;

    private Boolean isSocial;

    private String provider;
    @JsonProperty("user_id")
    private String userId;

}
