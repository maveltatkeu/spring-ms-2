package com.luwings.springsecurityhttpbasicauth.model;

import com.luwings.springsecurityhttpbasicauth.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements UserDetails {
    private final UserEntity userEntity;
    private final List<GrantedAuthority> authorities;

    public AuthenticatedUser(UserEntity userEntity, List<GrantedAuthority> authorities) {
        this.userEntity = userEntity;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }
}