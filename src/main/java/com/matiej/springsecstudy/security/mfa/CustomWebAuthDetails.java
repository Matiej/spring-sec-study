package com.matiej.springsecstudy.security.mfa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthDetails extends WebAuthenticationDetails {
    private final String VERIFICATION_PARAM = "Code";
    private String verificationCode;
    public CustomWebAuthDetails(HttpServletRequest req) {
        super(req);
        this.verificationCode = req.getParameter(VERIFICATION_PARAM);
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
