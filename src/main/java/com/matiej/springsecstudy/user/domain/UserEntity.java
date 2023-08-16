package com.matiej.springsecstudy.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matiej.springsecstudy.email.domain.EmailEntity;
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
    private String name;
    @JsonIgnore // in order to not display the password anywhere
    private String password;
    @JsonIgnore
    private String matchingPassword;
    @Column(name = "user_email")
    private String userEmail;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "role_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();
    private Boolean enabled;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<UserToken> tokens = new HashSet<>();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<EmailEntity> emails = new HashSet<>();

    public UserEntity(String name, String password, String matchingPassword, String userEmail) {
        this.name = name;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(password, that.password) && Objects.equals(matchingPassword, that.matchingPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, matchingPassword);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addToken(UserToken token) {
        this.tokens.add(token);
    }
    public void addEmail(EmailEntity email) {
        this.emails.add(email);
    }
}
