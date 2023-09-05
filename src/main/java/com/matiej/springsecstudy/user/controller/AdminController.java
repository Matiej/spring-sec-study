package com.matiej.springsecstudy.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

//    @RequestMapping("/secured")
//    public ModelAndView getSecuredPage(@RequestBody BindingResult result) {
//        if (result.hasErrors()) {
//            return new ModelAndView("unauthorized", "formErrors", result.getAllErrors());
//        }
//        return new ModelAndView("securedPage");
//    }

    @RequestMapping("/secured")
    public String getSecuredPage() {
          return "securedPage";
    }
}
