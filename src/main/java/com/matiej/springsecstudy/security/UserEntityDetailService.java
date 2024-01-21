package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.security.defaultusers.DefaultAdmin;
import com.matiej.springsecstudy.security.defaultusers.RunAsReporterUser;
import com.matiej.springsecstudy.security.defaultusers.TestUser;
import com.matiej.springsecstudy.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEntityDetailService implements UserDetailsService {
    private final UserService userService;
    private final DefaultAdmin defaultAdmin;
    private final TestUser testUser;
    private final RunAsReporterUser runAsReporterUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (defaultAdmin.getUsername().equalsIgnoreCase(username) ) {
            return defaultAdmin.defautUserToUser();
        }
        if(username.equals(testUser.getUsername())) {
            return testUser.defautUserToUser();
        }
        if(username.equals(runAsReporterUser.getUsername())) {
            return runAsReporterUser.defautUserToUser();
        }

        return userService.findByName(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));
    }
}
