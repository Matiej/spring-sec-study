package com.matiej.springsecstudy.email;

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

    public void sendActivatingEmail(SendingRequest request) {
        SimpleMailMessage simpleMailMessage = constructEmailMessage(request);
        try {
            mailSender.send(simpleMailMessage);
            log.info("Email has been sent successful to: " + Arrays.toString(simpleMailMessage.getTo()));
        } catch (Exception e) {
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
