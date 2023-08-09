package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    @GetMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new RegisterUserCommand());
    }

    @PostMapping(value = "/user/register")
    public ModelAndView registerUser(@Valid final RegisterUserCommand user, final BindingResult result,
                                     HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            user.setRequest(request);
            UserQueryResponse userQueryResponse = userService.registerNewUser(user);
        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping(value = "resetPassword")
    @ResponseBody
    public ModelAndView resetPassword(HttpServletRequest request, @RequestParam("email") String email,
                                      RedirectAttributes redirectAttributes) {
//        userService.findByEmail(email).map(userQueryResponse -> {
//            String token = UUID.randomUUID().toString();
//            PasswordR
//        })
        //todo FINISH
        return null;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

}
