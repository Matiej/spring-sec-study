package com.matiej.springsecstudy.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PathController {
    @RequestMapping("/")
    public String home() {
        return "redirect:/user";
    }

    @RequestMapping("/unauthorized")
    public ModelAndView unauthorizedAccess(HttpServletRequest request) {
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");
        return new ModelAndView("unauthorized", "errorMessage", errorMessage);
    }
}
