package com.example.todoz.controllers;

import com.example.todoz.services.MessageService;
import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {

    private final MessageService messageService;

    public NotificationController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody Subscription subscription) {
        System.out.println("New subscription: " + subscription);
        messageService.subscribe(subscription);
        messageService.sendNotifications();
        return "redirect:/";
    }

}
