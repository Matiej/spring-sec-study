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
    @Column(length = 3000)
    private String content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmailEntity that = (EmailEntity) o;
        return Objects.equals(emailTo, that.emailTo) && Objects.equals(emailCc, that.emailCc) && Objects.equals(sender, that.sender) && Objects.equals(subject, that.subject) && Objects.equals(content, that.content) && emailType == that.emailType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), emailTo, emailCc, sender, subject, content, emailType);
    }

    public static EmailEntity toEmailEntity(SimpleMailMessage mailMessage, EmailType emailType) {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmailTo(String.join(",", Objects.requireNonNull(mailMessage.getTo())));
        emailEntity.setSubject(mailMessage.getSubject());
        emailEntity.setContent(mailMessage.getText());
        emailEntity.setEmailCc(mailMessage.getCc() != null ? String.join(",", mailMessage.getCc()) : "");
        emailEntity.setEmailType(emailType);
        emailEntity.setSender(mailMessage.getFrom());
        emailEntity.setLastAttemptDate(LocalDateTime.now());
        return emailEntity;
    }

    public void incrementAttempt() {
        this.attempts++;
    }
}
