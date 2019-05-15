package com.example.bid.controller;

import com.example.bid.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdministrationHomeController {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AdministrationHomeController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String getAdministrationPage() {
        return "administration/home";
    }
}
