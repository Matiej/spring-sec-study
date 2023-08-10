package com.matiej.springsecstudy.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ActivateEmail implements Email {
    private static final String CONTENT = "Please activate your account using the following link: ";
    private static final String SUBJECT = "Account Activation";
    private final String recipient;
    private final String activateUrl;
    private final SendingService emailService;

    @Override
    public void sendEmail() {
        emailService.sendActivatingEmail(this.convertToSendingRequest());
    }

    private SendingRequest convertToSendingRequest() {
        final String content = CONTENT + this.activateUrl;
        return new SendingRequest(content, this.recipient, SUBJECT);
    }
}

