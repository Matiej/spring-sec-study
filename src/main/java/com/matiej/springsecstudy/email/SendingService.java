package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.database.EmailEntityRepository;
import com.matiej.springsecstudy.email.domain.EmailEntity;
import com.matiej.springsecstudy.email.domain.EmailStatus;
import com.matiej.springsecstudy.email.domain.EmailType;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.database.UserRepository;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

    public void sendAnEmail(SendingRequest request) {
        SimpleMailMessage simpleMailMessage = constructEmailMessage(request);

        EmailEntity emailEntity = EmailEntity.toEmailEntity(simpleMailMessage, request.getEmailType());
        UserEntity user = request.getUser();
        emailEntity.setUser(user);
        emailEntity.incrementAttempt();

        try {
            mailSender.send(simpleMailMessage);
            log.info("Email has been sent successful to: " + Arrays.toString(simpleMailMessage.getTo()));
            emailEntity.setStatus(EmailStatus.SENT);
            emailRepository.save(emailEntity);
            user.addEmail(emailEntity);
            userRepository.save(user);
        } catch (Exception e) {
            emailEntity.setStatus(EmailStatus.ERROR);
            emailRepository.save(emailEntity);
            user.addEmail(emailEntity);
            userRepository.save(user);
            log.error("Failed send activation email to: " + request.getRecipient());
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
