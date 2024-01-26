package com.example.todoz.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class NotificationScheduling {

    private final NotificationService notificationService;
    private final UserSubscriptionService userSubscriptionService;
    private final MessageService messageService;

    public NotificationScheduling(NotificationService notificationService, UserSubscriptionService userSubscriptionService, MessageService messageService) {
        this.notificationService = notificationService;
        this.userSubscriptionService = userSubscriptionService;
        this.messageService = messageService;
    }

    //    @Scheduled(cron = "0 30 8 * * *")
    @Scheduled(fixedRate = 10000)
    public void sendMorningNotifications() {

        System.out.println("The message was sent" + LocalTime.now());

        userSubscriptionService.getAll().forEach(userSub ->
                notificationService.sendNotification(userSub, messageService.getMorningNotification(userSub.getUser())));

    }

//    @Scheduled(cron = "0 00 12 * * *")
//    public void sendNoonNotifications() {
//
//
//        userSubscriptionService.getAll().forEach(userSub ->
//                notificationService.sendNotification(userSub, messageService.getNoonNotification(userSub.getUser())));
//
//        System.out.println("The message was sent" + LocalTime.now());
//    }
}
