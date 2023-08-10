package com.matiej.springsecstudy.email;

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
}
