package com.matiej.springsecstudy.email.application;

import com.matiej.springsecstudy.email.EmailType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SendEmailCommand {
    private final EmailType emailType;
    private final String recipient;
    private final String urlToClick;

}
