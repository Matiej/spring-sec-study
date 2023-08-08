package com.matiej.springsecstudy.user.controller;

import com.matiej.springsecstudy.user.application.UserQueryResponse;
import com.matiej.springsecstudy.user.application.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

    @GetMapping(value = "/reg", params = "form")
    public String createForm(@ModelAttribute CreateUserCommand createUserCommand) {
        System.out.println(createUserCommand);
        return "users/form";
    }

    @PostMapping("/reg")
    public ModelAndView create(@Valid CreateUserCommand user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("users/form", "formErrors", result.getAllErrors());
        }
        UserQueryResponse savedUser = userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/user/{user.id}", "user.id", savedUser.getId());
    }

    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") String id) {
        userService.deleteUser(Long.valueOf(id));
        return new ModelAndView("redirect:/");
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") String id, ModifyUserCommand user) {
        Long userId = Long.valueOf(id);
        return new ModelAndView("users/form", "user", user);
    }


    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
