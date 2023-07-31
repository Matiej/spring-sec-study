package com.matiej.springsecstudy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String role;

    RoleType(RoleType role) {
        this.role = role.getRole();
    }
}
