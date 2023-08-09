package com.matiej.springsecstudy.user.domain;

import com.matiej.springsecstudy.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken extends BaseEntity {
    private String token;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
    private LocalDateTime expiryDate;

    public VerificationToken(String token, UserEntity user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }


}
