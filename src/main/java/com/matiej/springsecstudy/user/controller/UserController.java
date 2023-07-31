package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ModelAndView list() {
        List<UserQueryResponse> userQueryResponses = UserQueryResponse.convertToResponse(userService.findAll());
        return new ModelAndView("users/list", "users", userQueryResponses);
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") String id) {
        Optional<UserQueryResponse> queryResponseOptional = userService.findById(Long.valueOf(id));
        UserQueryResponse user = queryResponseOptional.get();
        return new ModelAndView("users/view", "user", user);
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute CreateUserCommand createUserCommand) {
        return "users/form";
    }

    @PostMapping
    public ModelAndView create(@Valid CreateUserCommand user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form", "formErrors", result.getAllErrors());
        }
        UserQueryResponse savedUser = userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/{user.id}", "user.id", savedUser.getId());
    }

    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    @RequestMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") CreateUserCommand user) {
        return new ModelAndView("users/form", "user", user);
    }
}
