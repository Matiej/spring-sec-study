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
public class VerificationToken extends BaseEntity {
    private String token;
    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
    private LocalDateTime expiryDate;

    public VerificationToken(String token, UserEntity user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VerificationToken that = (VerificationToken) o;
        return Objects.equals(token, that.token) && Objects.equals(user, that.user) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, user, expiryDate);
    }
}
