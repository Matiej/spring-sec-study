package com.matiej.springsecstudy.email;

import com.matiej.springsecstudy.email.domain.EmailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResendingRequest {
    private EmailEntity email;
}
