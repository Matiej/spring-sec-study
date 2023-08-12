package com.matiej.springsecstudy.user.domain;

import com.matiej.springsecstudy.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserToken extends BaseEntity {
    private String token;
    @ManyToOne()
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
    private LocalDateTime expiryDate;
    @Enumerated
    private TokenType tokenType;

    public UserToken(String token, UserEntity user, LocalDateTime expiryDate, TokenType tokenType) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.tokenType = tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserToken that = (UserToken) o;
        return Objects.equals(token, that.token) && Objects.equals(user, that.user) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, user, expiryDate);
    }
}
