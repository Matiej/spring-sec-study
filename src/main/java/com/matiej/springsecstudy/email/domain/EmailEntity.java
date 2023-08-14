package com.matiej.springsecstudy.email.domain;

import com.matiej.springsecstudy.global.jpa.BaseEntity;
import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "email")
@Entity(name = "email")
public class EmailEntity extends BaseEntity {
    private String emailTo;
    private String emailCc;
    private String sender;
    private String subject;
    @Enumerated(EnumType.STRING)
    private EmailType emailType;
    private LocalDateTime sentDate;
    private LocalDateTime lastAttemptDate;
    @Enumerated(EnumType.STRING)
    private EmailStatus status;
    private int attempts;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public static EmailEntity toEmailEntity(SimpleMailMessage mailMessage, EmailType emailType) {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmailTo(String.join(",", Objects.requireNonNull(mailMessage.getTo())));
        emailEntity.setSubject(mailMessage.getSubject());
        emailEntity.setEmailCc(mailMessage.getCc() != null ? String.join(",", mailMessage.getCc()) : "");
        emailEntity.setEmailType(emailType);
        emailEntity.setSender(mailMessage.getFrom());
        emailEntity.setSentDate(LocalDateTime.now());
        emailEntity.setLastAttemptDate(LocalDateTime.now());
        return emailEntity;
    }

    public void incrementAttempt() {
        this.attempts++;
    }
}
