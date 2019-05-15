package com.example.bid.controller;

import com.example.bid.domain.Role;
import com.example.bid.domain.User;
import com.example.bid.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdministrationUserController {
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AdministrationUserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String getManageUserPage(
            Model model,
            @RequestParam(required = false) String[] messages
    ) {
        List<User> users = userRepository.getUsersByRoles(Collections.singleton(Role.USER));
        if (!users.isEmpty()) {
            model.addAttribute("users", users);
        }
        model.addAttribute("messages", (messages == null) ? new ArrayList<>() : Arrays.asList(messages));
        return "administration/users";
    }

    @PostMapping("/new")
    public RedirectView createUser(
            @RequestParam(name = "username")String username,
            @RequestParam(name = "password", defaultValue = "password")String password,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            String firstName = Character.toUpperCase(username.charAt(0)) + username.substring(1);
            String reversedFirstName = new StringBuilder(username).reverse().toString();
            String lastName = Character.toUpperCase(reversedFirstName.charAt(0)) + reversedFirstName.substring(1);
            String fullName = firstName + " " + lastName;
            user.setFullName(fullName);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Collections.singleton(Role.USER));
            user.setActive(true);
            userRepository.save(user);
            messages.add("Created new user (username:" + username + "-password: " + password + ")");
        } else {
            messages.add("The user with the name " + username + " already exists!");
        }
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/user");
    }

    @GetMapping("/info")
    public String getUserInfo(
            @RequestParam("username")String username,
            Model model,
            @RequestParam(required = false) String[] messages
    ) {
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("messages", (messages == null) ? new ArrayList<>() : Arrays.asList(messages));
        return "administration/user";
    }

    @PostMapping("/password")
    public RedirectView changePassword(
            @RequestParam(name = "username")String username,
            @RequestParam(name = "password")String password,
            @RequestParam(name = "confirm")String confirm,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        if (password.equals(confirm)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            messages.add("Password successfully updated for " + username + ".");
        } else {
            messages.add("Passwords don't match. Please, try again!");
        }
        attributes.addAttribute("username", username);
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/user/info");
    }

    @PostMapping("/activate")
    public RedirectView activateUser(
            @RequestParam(name = "username")String username,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        user.setActive(true);
        userRepository.save(user);
        messages.add("User " + username + " successfully activated!");
        attributes.addAttribute("username", username);
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/user/info");
    }

    @PostMapping("/deactivate")
    public RedirectView deactivateUser(
            @RequestParam(name = "username")String username,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        user.setActive(false);
        userRepository.save(user);
        messages.add("User " + username + " successfully deactivated!");
        attributes.addAttribute("username", username);
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/user/info");
    }

    @PostMapping("/delete")
    public RedirectView deleteUser(
            @RequestParam(name = "username")String username,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
        messages.add("User " + username + " successfully deleted!");
        attributes.addAttribute("username", username);
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/user");
    }

    private static String[] toStringArray(List<String> arrayList) {
        String[] str = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++)
            str[i] = arrayList.get(i);
        return str;
    }
}
