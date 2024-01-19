package com.example.todoz.controllers;

import com.example.todoz.models.User;
import com.example.todoz.services.MessageService;
import com.example.todoz.services.UserService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class NotificationController {

    private final MessageService messageService;
    private final UserService userService;

    public NotificationController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody Subscription subscription, Principal principal) {
        System.out.println("New subscription: " + subscription.keys.auth);
        messageService.subscribe(subscription, getUser(principal));
        return "redirect:/";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

}
