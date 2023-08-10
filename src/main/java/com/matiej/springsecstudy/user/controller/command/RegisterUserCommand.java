package com.matiej.springsecstudy.user.controller.command;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@NoArgsConstructor
public class RegisterUserCommand extends UserCommand{

    @NotEmpty(message = "password is required.")
    private String password;
    @NotEmpty(message = "matchingPassword is required.")
    private String matchingPassword;
    private HttpServletRequest request;

    @Override
    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode(this.password), passwordEncoder.encode(this.matchingPassword),
                this.email);
        userEntity.setEnabled(isEnabled);
        return userEntity;
    }
}
