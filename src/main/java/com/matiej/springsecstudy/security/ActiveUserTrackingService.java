package com.matiej.springsecstudy.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .map(principal -> (UserDetails) principal)
                .map(UserDetails::getUsername)
                .toList();
    }
}
