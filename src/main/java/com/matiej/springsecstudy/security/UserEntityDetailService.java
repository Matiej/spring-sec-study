package com.matiej.springsecstudy.security;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo delete test user or ADMIN!!!
        if (defaultAdmin.getUsername().equalsIgnoreCase(username) ) {
            return defaultAdmin.adminToUser();
        }
        if(username.equals(testUser.getUsername())) {
            return testUser.testUserToUser();
        }

        return userService.findByName(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));
    }
}
