package com.matiej.springsecstudy.user.controller.command;

import com.matiej.springsecstudy.global.validators.MatchingPassword;
import com.matiej.springsecstudy.global.validators.PasswordsValidator;
import com.matiej.springsecstudy.user.domain.Role;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@MatchingPassword
public class RegisterUserCommand extends UserCommand implements CreateUserMatchPass{

    @PasswordsValidator(message = "Password is required")
    private String password;
    private String matchingPassword;

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    @Override
    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode(this.password), passwordEncoder.encode(this.matchingPassword),
                this.email);
        userEntity.setEnabled(isEnabled);
        return userEntity;
    }

    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled, Role... roles) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode(this.password), passwordEncoder.encode(this.matchingPassword),
                this.email);
        userEntity.setEnabled(isEnabled);
        Set<Role> rolesSet = Arrays.stream(roles).collect(Collectors.toSet());
        userEntity.setRoles(rolesSet);
        return userEntity;
    }
}
