package com.matiej.springsecstudy.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordResetEmail implements Email{
    private static final String CONTENT = "Please open the following URL to reset your password: \\r\\n\"  ";
    private static final String SUBJECT = "Reset your Password";
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
