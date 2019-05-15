package com.example.bid.controller;

import com.example.bid.domain.User;
import com.example.bid.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final AdRepository adRepository;

    public HomeController(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @GetMapping("")
    public String getHomePage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((User)((UserDetails)principal)).getFullName();
        String avatar = ((User)((UserDetails)principal)).getAvatar();
        model.addAttribute("ads", adRepository.findAll());
        model.addAttribute("username", username);
        model.addAttribute("avatar", avatar);
        return "home/home";
    }
}
