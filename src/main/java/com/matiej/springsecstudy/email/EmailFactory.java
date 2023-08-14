package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.application.SendEmailCommand;

public class EmailFactory {

    public static Email createEmail(SendEmailCommand command, SendingService sendingService) {
        return switch (command.getEmailType()) {
            case ACTIVATE -> new ActivateEmail(command.getRecipient(), command.getUrlToClick(), sendingService, command.getUser());
            case PASSWORD_RESET ->
                    new PasswordResetEmail(command.getRecipient(), command.getUrlToClick(), sendingService, command.getUser());
            default -> throw new IllegalArgumentException("Invalid email type");
        };
    }


}
