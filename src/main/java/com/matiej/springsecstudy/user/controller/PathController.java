package com.matiej.springsecstudy.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PathController {
    @RequestMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String homePage() {
        return "homePage";
    }

    @RequestMapping("/cert")
    public String certPage() {
        return "CertPage";
    }

    @RequestMapping("/unauthorized")
    public ModelAndView unauthorizedAccess(HttpServletRequest request) {
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");
        return new ModelAndView("unauthorized", "errorMessage", errorMessage);
    }
}
