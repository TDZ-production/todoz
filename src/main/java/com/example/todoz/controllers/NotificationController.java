//package com.example.todoz.controllers;
//
//import com.example.todoz.services.MessageService;
//import nl.martijndwars.webpush.Subscription;
//import org.springframework.boot.SpringApplication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.security.Principal;
//
//@Controller
//public class NotificationController {
//
//    private final MessageService messageService;
//
//    public NotificationController(MessageService messageService) {
//        this.messageService = messageService;
//    }
//
//    @PostMapping("/subscribe")
//    public String subscribe(@RequestBody Subscription subscription, Principal principal) {
//        System.out.println("New subscription: " + subscription.keys.auth);
//        messageService.subscribe(subscription, user);
//        messageService.sendNotifications(principal, subscription);
//        return "redirect:/";
//    }
//
//    @GetMapping("/send-notification")
//    public String sendNotification(Subscription subscription, Principal principal) {
//        messageService.sendNotifications(principal, subscription);
//        return "redirect:/";
//    }
//
//}
