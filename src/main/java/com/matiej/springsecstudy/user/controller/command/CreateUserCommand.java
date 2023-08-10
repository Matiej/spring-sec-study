package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserCommand extends UserCommand {

    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode("pass"),
                passwordEncoder.encode("pass"), this.email);
        userEntity.setEnabled(isEnabled);
        return userEntity;
    }
}
