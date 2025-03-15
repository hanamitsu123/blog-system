package com.example.blog_system.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog_system.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
      
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; 
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String email, 
                               @RequestParam String password, Model model) {
        // 既存のユーザーがいないかチェック
        if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "このメールアドレスは既に登録されています。");
            return "register";
        }

        // ユーザーを登録
        userService.registerUser(username, email, password);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 
    }
}