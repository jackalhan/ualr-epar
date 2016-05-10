package com.jackalhan.ualr.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by txcakaloglu on 5/6/16.
 */
@Controller
public class TestController {
    @RequestMapping("/general")
    public String general() {

        return "general";
    }
    @RequestMapping("/")
    public String index(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    @RequestMapping("/index")
    public String index2(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    @RequestMapping("/basic_form")
    public String basicForm() {

        return "basic_form";
    }
    @RequestMapping("/simple")
    public String simple() {

        return "simple";
    }

 @RequestMapping("/login")
    public String login() {

        return "login";
    }

/*    @RequestMapping("/master")
    public String master(Principal principal, Model model) {
        model.addAttribute("username", getUserName(principal));
        model.addAttribute("userroles", getUserRoles(principal));
        return "fragments/master";
    }

    @RequestMapping("/header")
    public String header() {
        return "fragments/header";
    }*/

    @RequestMapping("/workload")
    public String workload(Principal principal, Model model) {
        model.addAttribute("username", getUserName(principal));
        model.addAttribute("userroles", getUserRoles(principal));
        return "workload";
    }

    private String getUserName(Principal principal) {
        if (principal == null) {
            return "anonymous";
        } else {

            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println(grantedAuthority.getAuthority());
            }
            return principal.getName();
        }
    }

    private Collection<String> getUserRoles(Principal principal) {
        if (principal == null) {
            return Arrays.asList("none");
        } else {

            Set<String> roles = new HashSet<String>();

            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }
            return roles;
        }
    }
}
