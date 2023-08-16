package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.database.EmailEntityRepository;
import com.matiej.springsecstudy.email.domain.EmailEntity;
import com.matiej.springsecstudy.email.domain.EmailStatus;
import com.matiej.springsecstudy.email.domain.EmailType;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.domain.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendingService {
    private final JavaMailSender mailSender;
    private final Environment environment;
    private final EmailEntityRepository emailRepository;
    private final UserRepository userRepository;
    @Value("${email.resending.attemptsLimit}")
    private int attemptsLimit;

    public void sendAnEmail(SendingRequest request) {
        SimpleMailMessage simpleMailMessage = constructEmailMessage(request);

        EmailEntity emailEntity = EmailEntity.toEmailEntity(simpleMailMessage, request.getEmailType());
        UserEntity user = request.getUser();
        emailEntity.setUser(user);
        send(simpleMailMessage, emailEntity, user);
    }

    @Transactional
    public void reSendEmail(ResendingRequest request) {
        EmailEntity email = request.getEmail();
        SimpleMailMessage simpleMailMessage = constructEmailMessage(new SendingRequest(email.getContent(),
                email.getEmailTo(), email.getSubject()));
        send(simpleMailMessage, email, email.getUser());
    }

    private void send(SimpleMailMessage simpleMailMessage,
                      EmailEntity emailEntity, UserEntity user) {
        String recipients = Arrays.toString(simpleMailMessage.getTo());
        emailEntity.incrementAttempt();
        try {
            mailSender.send(simpleMailMessage);
            log.info("Email has been sent successful to: " + recipients);
            emailEntity.setStatus(EmailStatus.SENT);
            emailEntity.setSentDate(LocalDateTime.now());
            saveAll(emailEntity, user);
        } catch (Exception e) {
            emailEntity.setStatus(emailEntity.getAttempts() >= attemptsLimit ? EmailStatus.ABANDONED: EmailStatus.ERROR);
            emailEntity.setLastAttemptDate(LocalDateTime.now());
            saveAll(emailEntity, user);
            log.error("Failed send activation email to: " + recipients);
        }
    }

    private void saveAll(EmailEntity emailEntity, UserEntity user) {
        emailRepository.save(emailEntity);
        if (emailEntity.getAttempts() <= 1) {
            user.addEmail(emailEntity);
            userRepository.save(user);
        }
    }

    private SimpleMailMessage constructEmailMessage(SendingRequest request) {
        final String recipientAddress = request.getRecipient();
        final String subject = request.getSubject();
        final String content = request.getContent();
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(content);
        email.setSentDate(new Date());
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }

}
