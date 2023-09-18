package com.matiej.springsecstudy.security;

import com.matiej.springsecstudy.user.database.RolesRepository;
import com.matiej.springsecstudy.user.domain.Role;
import com.matiej.springsecstudy.user.domain.RoleType;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//        By default,the main authentication provider is going to be the DaoAuthenticationProvider.
//        We're going to now roll our own provider - which simply authenticates against a third-party system. This is going to replace the default provider.
//        First,let's discuss the contract of the authenticate method in the provider:
//        if authentication succeeds,a full Authentication object(with credentials)is expected as the return
//        if the provider doesn’t support the Authentication input,it will return null(and the next provider will be tried)
//        if the provider does support it and we attempt authentication and fail-AuthenticationException
//        When we're talking about the exception, it’s important to understand that is the base class for many, many more specific exceptions.
//        In a production implementation-for a real integration with a third party authentication service-where you’ll have a lot more info to work with.
//        So,we'll need to throw very specific exceptions based on the actual problem that occurred.

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final RolesRepository repository;
    private static final String THIRD_PARTY_USER_NAME = "3rdUser";
    private static final String THIRD_PARTY_USER_ROLE = "ROLE_SECURED";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider invoked with authentication: {}", authentication);
        String name = authentication.getName();
        String pass = authentication.getCredentials().toString();
        if (!supportsAuthentication(authentication)) {
            return null;
        }
        if (doAuthenticationAgainstThirdPartySystem(name)) {
            UserEntityDetails userDetails = prepareUserDetails(authentication);
            return new UsernamePasswordAuthenticationToken(userDetails, pass, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Authentication against third party system failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private UserEntityDetails prepareUserDetails(Authentication authentication) {

        Set<Role> roles = repository.findAll().stream().
                filter(p -> THIRD_PARTY_USER_ROLE.equals(p.getRoleName())).
                collect(Collectors.toSet());
        if(roles != null && roles.size() < 1) {
            roles.add(new Role(RoleType.ROLE_SECURED));
        }
        //assuming that user comes from table, by user name
        UserEntity ue = new UserEntity();
        ue.setName(authentication.getName());
        ue.setUserEmail("some3rdemail@third.eu");
        ue.setEnabled(true);
        ue.setRoles(roles);
        ue.setPassword(authentication.getCredentials().toString());

        return new UserEntityDetails(ue);
    }

    private boolean doAuthenticationAgainstThirdPartySystem(String name) {
        //assuming that all auth is correct. Just for test custom provider
        return THIRD_PARTY_USER_NAME.equals(name);
    }

    private boolean supportsAuthentication(Authentication authentication) {
        return true;
    }
}
