package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.domain.EmailType;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class SendingRequest {
    private final String content;
    private final String recipient;
    private final String subject;
    private UserEntity user;
    private EmailType emailType;

    public SendingRequest(String content, String recipient, String subject) {
        this.content = content;
        this.recipient = recipient;
        this.subject = subject;
    }
}
