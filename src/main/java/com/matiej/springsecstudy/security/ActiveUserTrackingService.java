package com.matiej.springsecstudy.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActiveUserTrackingService {
    private final SessionRegistry registry;

    public List<String> getAllActiveUsers() {
        //todo attention, spring core userdetails User is returned here, not any other user
        List<Object> allPrincipals = registry.getAllPrincipals();

        return allPrincipals.stream()
                .filter(p-> !registry.getAllSessions(p, false).isEmpty()) // get all active users
                .map(principal -> (User) principal)
                .map(User::getUsername)
                .toList();
    }
}
