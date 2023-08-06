package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.user.application.UserService;
import lombok.AllArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (defaultAdmin.getUsername().equalsIgnoreCase(username)) {
            return defaultAdmin.adminToUser();
        }
        return userService.findByName(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));
    }
}
