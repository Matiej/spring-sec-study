package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import com.matiej.springsecstudy.user.controller.command.CreateUserCommand;
import com.matiej.springsecstudy.user.controller.command.ModifyUserCommand;
import com.matiej.springsecstudy.user.domain.UserToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    @GetMapping()
    public ModelAndView list() {
        List<UserQueryResponse> userQueryResponses = UserQueryResponse.convertToResponse(userService.findAll());
        return new ModelAndView("users/list", "users", userQueryResponses);
    }

    @GetMapping("/{id}")
    public ModelAndView view(@PathVariable("id") String id) {
        Long userId = Long.valueOf(id);
        Optional<UserQueryResponse> queryResponseOptional = userService.findById(userId);
        UserQueryResponse foundUser = queryResponseOptional.get();
        return new ModelAndView("users/view", "user", foundUser);

    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute CreateUserCommand createUserCommand) {
        System.out.println(createUserCommand);
        return "users/form";
    }

    @GetMapping(params = "form-mod")
    public String modify(@ModelAttribute ModifyUserCommand modifyUserCommand) {
        return "users/form-mod";
    }

    @PostMapping()
    public ModelAndView create(@Valid CreateUserCommand user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form", "formErrors", result.getAllErrors());
        }
        UserQueryResponse savedUser = userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/user/{user.id}", "user.id", savedUser.getId());
    }

    @PostMapping(value = "/mod")
    public ModelAndView modify(@Valid ModifyUserCommand user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form-mod", "formErrors", result.getAllErrors());
        }
        UserQueryResponse savedUser = userService.update(user);
        redirect.addFlashAttribute("globalMessage", "Successfully modified user");
        return new ModelAndView("redirect:/user/{user.id}", "user.id", savedUser.getId());
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") String id, ModifyUserCommand user) {
        Long userId = Long.valueOf(id);
        return new ModelAndView("users/form-mod", "user", user);
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") String id) {
        userService.deleteUser(Long.valueOf(id));
        return new ModelAndView("redirect:/");
    }

    @PostMapping(value = "/savePassword")
    @ResponseBody
    public ModelAndView savePassword(@RequestParam("password") final String password,
                                     @RequestParam("passwordConfirmation") final String passwordConfirmation,
                                     @RequestParam("token") final String token, RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordConfirmation)) {
            return new ModelAndView("resetPassword", Map.of("errorMessage", "Passwords do not match"));
        }
        return userService.getPasswordResetToken(token).map(passwordResetToken -> {
            if (passwordResetToken.getUser() == null) {
                redirectAttributes.addFlashAttribute("message", "Unknown user");
            }
            userService.changeUserPassword(passwordResetToken.getUser(), password);
            redirectAttributes.addFlashAttribute("message", "Password reset successfully");
            return new ModelAndView("redirect:/reg/login");
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("message", "Invalid token");
            return new ModelAndView("redirect:/reg/login");
        });
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
