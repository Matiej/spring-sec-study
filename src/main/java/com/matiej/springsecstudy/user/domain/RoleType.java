package com.matiej.springsecstudy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER"),
    ADMIN("ADMIN"),
    USER("USER"),
    ROLE_SECURED("ROLE_SECURED");

    private final String role;

    RoleType(RoleType role) {
        this.role = role.getRole();
    }
}
