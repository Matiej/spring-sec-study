package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Setter
@Getter
public class RegisterUserCommand {
    @NotEmpty(message = "Username is required.")
    private String username;
    @NotEmpty(message = "Email is required.")
    private String email;
    @NotEmpty(message = "password is required.")
    private String password;
    @NotEmpty(message = "matchingPassword is required.")
    private String matchingPassword;

    public UserEntity toUserEntity(PasswordEncoder passwordEncoder) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode(this.password), passwordEncoder.encode(this.matchingPassword), this.email);
        userEntity.setCreatedAt(LocalDateTime.now());
        return userEntity;
    }
}
