package com.example.todoz.controllers;

import com.example.todoz.models.User;
import com.example.todoz.services.NotificationService;
import com.example.todoz.services.UserService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody Subscription subscription, Principal principal) {
        notificationService.subscribe(subscription, getUser(principal));
        return "redirect:/";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

}
