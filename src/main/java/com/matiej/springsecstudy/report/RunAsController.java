package com.matiej.springsecstudy.report;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/runas")
@RequiredArgsConstructor
public class RunAsController {
    private final RuAsService ruAsService;

    @GetMapping
    @Secured({"ROLE_RUN_AS_REPORTER"}) //todo RUN_AS prefix is very importat. It ruuns - runAsAuthenticationProvider() and all stuff around like service etc
    public ModelAndView tryRunAs() {
        //do magic stuff for report
        // run atuh service
        String runAsServiceClass = ruAsService.getCurrentUser().getAuthorities().toString();

        return new ModelAndView( "reportPage", "report", runAsServiceClass);
    }
}
