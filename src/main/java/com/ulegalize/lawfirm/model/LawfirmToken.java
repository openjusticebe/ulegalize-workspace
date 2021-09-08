package com.ulegalize.lawfirm.model;

import com.ulegalize.enumeration.DriveType;
import com.ulegalize.security.EnumRights;
import com.ulegalize.security.UlegalizeToken;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@ToString
public class LawfirmToken extends UlegalizeToken implements UserDetails {

    private static final long serialVersionUID = 1L;

    public LawfirmToken(Long userId, String username, String userEmail,
                        String vcKey, String password, Boolean enabled,
                        List<EnumRights> enumRights, String token, Boolean temporary,
                        String language, String symbolCurrency, String fullname,
                        DriveType driveType, String dropboxToken) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.vcKey = vcKey;
        this.password = password;
        this.enabled = enabled;
        this.enumRights = enumRights;
        this.token = token;
        this.temporary = temporary;
        this.language = language;
        this.symbolCurrency = symbolCurrency;
        this.fullname = fullname;
        this.driveType = driveType;
        this.dropboxToken = dropboxToken;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (EnumRights enumRights : this.enumRights) {
            authorities.add(new SimpleGrantedAuthority(enumRights.name()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

}