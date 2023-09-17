package com.matiej.springsecstudy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider invoked with authentication: {}", authentication);
        String name = authentication.getName();
        String pass = authentication.getCredentials().toString();
        if (!supportsAuthentication(authentication)) {
            return null;
        }
        if (doAuthenticationAgainstThirdPartySystem()) {
            return new UsernamePasswordAuthenticationToken(name, pass, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Authentication against third party system failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean doAuthenticationAgainstThirdPartySystem() {
        return true;
    }

    private boolean supportsAuthentication(Authentication authentication) {
        return true;
    }
}
