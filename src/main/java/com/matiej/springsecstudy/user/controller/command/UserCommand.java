package com.matiej.springsecstudy.user.controller.command;

import com.matiej.springsecstudy.user.domain.Role;
import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class UserCommand {
    @NotEmpty(message = "Username is required.")
    protected String username;
    @Email(message = "Email is required.")
    protected String email;
    @Size(min = 1, message = "At least one role must be selected.")
    private List<String> roles;


    public abstract UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled, Set<Role> roles);
}
