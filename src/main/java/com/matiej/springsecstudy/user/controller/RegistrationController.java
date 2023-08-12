package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.RegisterUserCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

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
    public ModelAndView registerUser(@Valid final RegisterUserCommand user, final BindingResult result,
                                     final HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("message", "Activation email has been sent to: " + user.getEmail());
        return new ModelAndView("redirect:/reg/login");
    }

    @GetMapping(value = "/registerConfirm")
    public ModelAndView registrationConfirm(final Model model, @RequestParam("token") final String token,
    RedirectAttributes redirectAttributes) {
        userService.confirmRegistration(token);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully and activated");
        return new ModelAndView("redirect:/reg/login");
    }


    @PostMapping(value = "/resetPassword")
    public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String email,
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
    public String forgotPassword(final HttpServletRequest request) {
        return "forgotPassword";
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("message", message);
        return "loginPage";
    }

}
