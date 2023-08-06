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
public class CreateUserCommand {

    private Long id;
    @NotEmpty(message = "Username is required.")
    private String username;
    @NotEmpty(message = "Email is required.")
    private String email;

    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode("pass"), passwordEncoder.encode("pass"), this.email);
        userEntity.setId(this.id);
        return userEntity;
    }
}
