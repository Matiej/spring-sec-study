package com.matiej.springsecstudy.web;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class VerificationCodeController {
    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "spring-sec-stud";
    private final UserService userService;

    @GetMapping("/code")
    @ResponseBody
    public Map<String, String> getQRUrl(@RequestParam("username") final String username) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<String, String>();

        Optional<UserQueryResponse> userQueryResponse = userService.findByEmail(username);
        if (userQueryResponse.isPresent()) {

            result.put("url", generateQRUrl(userQueryResponse.get().getSecret(),
                    userQueryResponse.get().getEmail()));
        } else {
            result.put("url", "");
        }

        return result;
    }

    private String generateQRUrl(String secret, String username) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, username, secret, APP_NAME), "UTF-8");
    }

}
