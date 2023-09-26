package com.matiej.springsecstudy.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class UserSecurityService {
//    private final AuthenticationManager authenticationManager;

    public boolean isOwnerOrAdmin(String owner, UserEntityDetails userEntityDetails) {
        return isAdmin(userEntityDetails) || isOwner(owner, userEntityDetails.getUsername());
    }

    public boolean isAdmin(UserEntityDetails userEntityDetails) {
        return userEntityDetails.getAuthorities().stream()
                .anyMatch(m-> m.getAuthority().equals("ADMIN"));
    }

    private boolean isOwner(String owner, String userName) {
        return StringUtils.equalsAnyIgnoreCase(owner, userName);
    }


}
