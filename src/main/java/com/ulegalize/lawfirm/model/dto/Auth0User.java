
package com.ulegalize.lawfirm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ulegalize.dto.AppMetadata;
import lombok.Data;

import java.util.List;

@Data
public class Auth0User {

    @JsonProperty("app_metadata")
    private AppMetadata appMetadata;

    private Boolean blocked;
    @JsonProperty("created_at")
    private String createdAt;

    private String email;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("given_name")
    private String givenName;

    private List<Identity> identities;
    @JsonProperty("last_ip")
    private String lastIp;
    @JsonProperty("last_login")
    private String lastLogin;
    @JsonProperty("logins_count")
    private Long loginsCount;

    private List<String> multifactor;

    private String name;

    private String nickname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("phone_verified")
    private Boolean phoneVerified;

    private String picture;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("user_id")
    private String userId;

    private String username;

}
