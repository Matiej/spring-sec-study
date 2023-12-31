package com.matiej.springsecstudy.user.controller.command;

import com.matiej.springsecstudy.user.domain.Role;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ModifyUserCommand extends UserCommand {
    private Long id;

    public UserEntity convertToUserEntity(PasswordEncoder passwordEncoder, boolean isEnabled, Set<Role> roles) {
        UserEntity userEntity = new UserEntity(this.username, passwordEncoder.encode("pass"),
                passwordEncoder.encode("pass"), this.email);
        userEntity.setId(id);
        userEntity.setEnabled(isEnabled);
        return userEntity;
    }
}
