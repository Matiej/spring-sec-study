package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.application.SendEmailCommand;

public class EmailFactory {

    public static Email createEmail(SendEmailCommand command, SendingService sendingService) {
        switch (command.getEmailType()) {
            case ACTIVATE:
                return new ActivateEmail(command.getRecipient(), command.getUrlToClick(), sendingService);
            default:
                throw new IllegalArgumentException("Invalid email type");
        }
    }


}
