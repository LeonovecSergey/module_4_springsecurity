package com.epam.learn.springsecurity.controller;

import com.epam.learn.springsecurity.model.UserEntity;
import com.epam.learn.springsecurity.reository.UserRepository;
import com.epam.learn.springsecurity.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class MainController {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @GetMapping("/info")
    public String info() {
        return "info";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/login")
    public String login(final ModelMap model, @RequestParam("error") final Optional<String> error) {
        error.ifPresent(e -> model.addAttribute("error", error));
        return "login";
    }

    @GetMapping("/blocked")
    public String blocked(Model model) {
        var users = userRepository.findAll();
        Map<String, LocalDateTime> blockedUsers = users.stream()
                .map(UserEntity::getName)
                .filter(loginAttemptService::isBlocked)
                .collect(Collectors.toMap(user -> user,
                        user -> loginAttemptService.getCachedValue(user).getBlockedTimestamp()));
        if (!CollectionUtils.isEmpty(blockedUsers)) {
            model.addAttribute("blockedUsers", blockedUsers);
        }
        return "blocked";
    }

}
