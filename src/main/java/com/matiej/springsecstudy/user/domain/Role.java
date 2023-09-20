package com.matiej.springsecstudy.user.domain;

import com.matiej.springsecstudy.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity(name = "roles")
@NoArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {
    @Column(unique = true)
    private String roleName;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "roles", targetEntity = UserEntity.class)
    private Set<UserEntity> userEntities = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    )
    private Set<Privilege> privileges = new HashSet<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(RoleType roleType) {
        this.roleName = roleType.name();
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
