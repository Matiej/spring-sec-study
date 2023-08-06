package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        return new UserEntity(this.username, passwordEncoder.encode(this.password), passwordEncoder.encode(this.matchingPassword), this.email);
    }
}
