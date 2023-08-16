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
@Entity(name = "token")
@Table(name = "token")
@NoArgsConstructor
public class UserToken extends BaseEntity {
    private String tokenName;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private LocalDateTime expiryDate;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    public UserToken(String tokenName, UserEntity user, LocalDateTime expiryDate, TokenType tokenType) {
        this.tokenName = tokenName;
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
        return Objects.equals(tokenName, that.tokenName) && Objects.equals(user, that.user) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tokenName, user, expiryDate);
    }
}
