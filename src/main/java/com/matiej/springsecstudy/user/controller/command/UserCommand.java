package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public abstract class UserCommand {
    @NotEmpty(message = "Username is required.")
    protected String username;
    @NotEmpty(message = "Email is required.")
    protected String email;

    public abstract UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled);
}
