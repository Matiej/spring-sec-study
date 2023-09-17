package com.matiej.springsecstudy.report;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuAsService {

    @Secured("ROLE_RUN_AS_REPORTER") //todo -> that role is very important. Prefix RUN_AS
    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
