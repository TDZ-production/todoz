package com.example.todoz.controllers;

import com.example.todoz.models.User;
import com.example.todoz.services.MessageService;
import com.example.todoz.services.NotificationService;
import com.example.todoz.services.UserService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class NotificationController {

    private final MessageService messageService;
    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationController(MessageService messageService, UserService userService, NotificationService notificationService) {
        this.messageService = messageService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestBody Subscription subscription, Principal principal) {
        System.out.println("New subscription: " + subscription.keys.auth);
        messageService.subscribe(subscription, getUser(principal));
        return "redirect:/";
    }

    @GetMapping("/send-notification")
    public String sendNotification() {
        messageService.sendNotifications();
        return "redirect:/";
    }
    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

}
