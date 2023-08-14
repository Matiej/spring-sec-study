package com.matiej.springsecstudy.email.application;

import com.matiej.springsecstudy.email.domain.EmailType;
import com.matiej.springsecstudy.user.domain.UserEntity;
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
    private final UserEntity user;

}
