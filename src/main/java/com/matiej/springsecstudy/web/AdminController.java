package com.matiej.springsecstudy.web;

import com.matiej.springsecstudy.user.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @RequestMapping("/secured")
    public ModelAndView getSecuredPage(Authentication authentication, HttpServletRequest request) {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        return new ModelAndView("admin/securedPage", "user", user);
    }

    @RequestMapping("/admin-settings")
    @PreAuthorize("hasRole('ADMIN') and principal.username=='admin'")
    public ModelAndView getAdminPage(Authentication authentication, HttpServletRequest request) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return new ModelAndView("admin/adminPage", "user", user);
    }

    @RequestMapping("/IPSecured")
    public ModelAndView getIPSecuredPage() {
        return new ModelAndView("admin/addressIPSecured", "dataMap", getHostAndIP());
    }

    private Map<String, String> getHostAndIP() {
        try {
            InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
            String hostAddress = loopbackAddress.getHostAddress();
            String hostName = loopbackAddress.getHostName();

            InetAddress ip = InetAddress.getLocalHost();
            String serverIPAddress = ip.getHostAddress();
            String servername = ip.getHostName();

            Map<String, String> localDataMap = new HashMap<>();
            localDataMap.put("hostAddress", hostAddress);
            localDataMap.put("hostName", hostName);
            localDataMap.put("servername", servername);
            localDataMap.put("serverIPAddress", serverIPAddress);
            return localDataMap;
        } catch (UnknownHostException e) {
            return new HashMap<>();
        }
    }
}
