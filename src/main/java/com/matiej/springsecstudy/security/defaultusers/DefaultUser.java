package com.matiej.springsecstudy.security.defaultusers;

import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Data
public abstract class DefaultUser {
    private String username;
    private String password;
    private Set<String> roles;

    public abstract User defautUserToUser();
}
