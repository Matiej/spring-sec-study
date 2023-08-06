package com.matiej.springsecstudy.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matiej.springsecstudy.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity(name = "users")
@Table(name = "users")
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore // in order to not display the password anywhere
    private String password;
    @JsonIgnore
    private String matchingPassword;
    private String email;
    //persistence mapping will come later with
    // db
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "role_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    public UserEntity(String username, String password, String matchingPassword, String email) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(matchingPassword, that.matchingPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, matchingPassword);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}
