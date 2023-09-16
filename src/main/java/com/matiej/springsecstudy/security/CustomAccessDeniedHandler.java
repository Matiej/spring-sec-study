package com.matiej.springsecstudy.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collectors;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final String ADMIN_URI = "/admin-settings";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String remoteUser = request.getRemoteUser();
        String requestURI = request.getRequestURI();
        Principal userPrincipal = request.getUserPrincipal();

        String errorMessage = "You don't have permission to access this page.";

        if (requestURI.contains("/admin-settings")) {
            String principalRoles = getPrincipalRoles(userPrincipal);
            String roles = Strings.isNotEmpty(principalRoles) ? principalRoles : "[no ROLES found]";
            errorMessage = "User: " + remoteUser + " with ROLES: " + roles + " doesn't have permission to access this page!";
        } else if (requestURI.contains("/IPSecured")) {
            errorMessage = "User: " + remoteUser + " with IP: " + request.getRemoteAddr() + " doesn't have permission to access this page!";
        } else if (requestURI.contains("/runas")) {
            errorMessage = "User: " + remoteUser + " with IP: " + request.getRemoteAddr() + " doesn't have permission to access this page! " +
                    "Access only with roles: ROLE_USER, RUN_AS_REPORTER";
        }
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/unauthorized");
    }

    private String getPrincipalRoles(Principal principal) {
        if (principal != null && principal instanceof Authentication) {
            Authentication auth = (Authentication) principal;
            return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        }
        return "";
    }
}
