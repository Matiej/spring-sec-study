package com.matiej.springsecstudy.security.defaultusers;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Data
@ConfigurationProperties("app.security.runas-user")
public class RunAsReporterUser extends DefaultUser {
    @Override
    public User defautUserToUser() {
        return runasUserToUser();
    }

    private User runasUserToUser() {
        return new User(
                getUsername(),
                getPassword(),
                getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()));
    }
}
