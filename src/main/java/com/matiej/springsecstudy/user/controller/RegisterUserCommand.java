package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

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

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity(this.username, this.password, this.matchingPassword, this.email);
        userEntity.setCreatedAt(LocalDateTime.now());
        return userEntity;
    }
}
