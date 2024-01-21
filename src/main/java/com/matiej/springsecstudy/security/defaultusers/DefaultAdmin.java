package com.matiej.springsecstudy.security.defaultusers;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Data
@ConfigurationProperties("app.security.admin")
public class DefaultAdmin extends DefaultUser {

    @Override
    public User defautUserToUser() {
        return adminToUser();
    }

    private User adminToUser() {
        return new User(
                getUsername(),
                getPassword(),
                getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()));
    }
}
