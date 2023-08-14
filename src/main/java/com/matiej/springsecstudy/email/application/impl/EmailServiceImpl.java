package com.matiej.springsecstudy.email.application.impl;

import com.matiej.springsecstudy.email.Email;
import com.matiej.springsecstudy.email.EmailFactory;
import com.matiej.springsecstudy.email.SendingService;
import com.matiej.springsecstudy.email.application.EmailService;
import com.matiej.springsecstudy.email.application.SendEmailCommand;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.application.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {
    private final SendingService sendingService;
    @Override
    public void sendEmail(SendEmailCommand command) {
        Email email = EmailFactory.createEmail(command, sendingService);
        email.sendEmail();
    }
}
