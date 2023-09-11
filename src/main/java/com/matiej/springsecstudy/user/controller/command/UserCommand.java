package com.matiej.springsecstudy.user.controller.command;

import com.matiej.springsecstudy.global.validators.MatchingPassword;
import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public abstract class UserCommand {
    @NotEmpty(message = "Username is required.")
    protected String username;
    @Email(message = "Email is required.")
    protected String email;

    public abstract UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled);
}
