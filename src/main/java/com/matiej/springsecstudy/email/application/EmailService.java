package com.matiej.springsecstudy.email.application;

public interface EmailService {
    void sendEmail(SendEmailCommand command);
}
