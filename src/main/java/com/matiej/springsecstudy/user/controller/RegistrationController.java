package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.RegisterUserCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/reg")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    @GetMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new RegisterUserCommand());
    }

    @PostMapping(value = "/register")
    public ModelAndView registerUser(@Valid RegisterUserCommand user, BindingResult result,
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

    @PostMapping(value = "/resetPassword")
    public ModelAndView resetPassword(HttpServletRequest request, @RequestParam("email") String email,
                                      RedirectAttributes redirectAttributes) {
//        userService.findByEmail(email).map(userQueryResponse -> {
//            String token = UUID.randomUUID().toString();
//            PasswordR
//        })
        System.out.println(email);
        //todo FINISH
        return null;
    }


    @GetMapping(value = "/forgotPassword")
    public String forgotPassword(HttpServletRequest request) {
        return "forgotPassword";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

}
