package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.domain.UserEntity;
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

import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/reg")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new CreateUserCommand());
    }

    @PostMapping(value = "/register")
    public ModelAndView registerUser(@Valid final CreateUserCommand user, final BindingResult result,
                                     final HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            UserQueryResponse userQueryResponse = userService.registerNewUser(user, request);
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
    @ResponseBody
    public ModelAndView resetPassword(HttpServletRequest request,
                                      @RequestParam("email") String userEmail,
                                      RedirectAttributes redirectAttributes) {
        userService.resetPassword(userEmail, request);
        redirectAttributes.addFlashAttribute("message",
                "You should receive an Password Reset Email shortly");
        return new ModelAndView("redirect:/reg/login");
    }

    @GetMapping(value = "/changePassword")
    public ModelAndView showChangePasswordPage(
            @RequestParam("id") long id,
            @RequestParam("token") String token,
            RedirectAttributes redirectAttributes) {

        return userService.getPasswordResetToken(token).map(resetPasswordToken -> {
            UserEntity user = resetPasswordToken.getUser();
            if (user.getId() != id) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
                return new ModelAndView("redirect:/reg/login");
            }
            if (resetPasswordToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your password token has expired");
                return new ModelAndView("redirect:/reg/login");
            }
            final ModelAndView view = new ModelAndView("resetPassword");
            view.addObject("token", token);
            return view;
        }).orElseGet(()-> {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return new ModelAndView("redirect:/reg/login");
        });
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

    @GetMapping("/logout")
    public String logout(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("message", message);
        return "logoutPage";
    }

}
