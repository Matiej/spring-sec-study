package com.matiej.springsecstudy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ADMIN"), USER("USER");

    private final String role;

    RoleType(RoleType role) {
        this.role = role.getRole();
    }
}
