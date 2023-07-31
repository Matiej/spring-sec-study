package com.matiej.springsecstudy.user.domain;

import com.matiej.springsecstudy.global.jpa.BaseEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role extends BaseEntity {
    private String roleName;
    //persistence mapping will come later with db
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserEntity> userEntities = new HashSet<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleName);
    }

    public void addUser(UserEntity user) {
        this.userEntities.add(user);
    }

    public void removeUser(UserEntity user) {
        this.userEntities.remove(user);
    }
}
