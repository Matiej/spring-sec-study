package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserCommand {

    private Long id;
    @NotEmpty(message = "Username is required.")
    private String username;
    @NotEmpty(message = "Email is required.")
    private String email;

    public UserEntity convertToUserEntity() {
        UserEntity userEntity = new UserEntity(this.username, "pass", "pass", this.email);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setId(this.id);
        return userEntity;
    }
}
