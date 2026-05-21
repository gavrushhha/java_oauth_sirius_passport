package com.example.authdemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            model.addAttribute("sub", getClaim(attributes, "sub"));
            model.addAttribute("phone", getClaim(attributes, "phone_number", "phone"));
            model.addAttribute("email", getClaim(attributes, "email"));
            model.addAttribute("name", getClaim(attributes, "name", "preferred_username", "given_name"));
            model.addAttribute("attributes", attributes);
        }
        return "profile";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "admin";
    }

    private String getClaim(Map<String, Object> attributes, String... keys) {
        for (String key : keys) {
            Object value = attributes.get(key);
            if (value != null && !value.toString().isBlank()) {
                return value.toString();
            }
        }
        return "Not provided";
    }
}
