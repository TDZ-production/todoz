package com.example.todoz.services;

import com.example.todoz.models.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationScheduling {

    private final NotificationService notificationService;
    private final UserSubscriptionService userSubscriptionService;
    private final MessageService messageService;

//    @Scheduled(fixedRate = 10000)
//    public void sendMorningNotifications() {
//
//        System.out.println("The message was sent" + LocalTime.now());
//
//        userSubscriptionService.getAll().forEach(userSub ->
//                notificationService.sendNotification(userSub, messageService.getNotification(userSub.getUser())));
//
//    }

    //    @Scheduled(cron = "0 30 8 * * *")
    @Scheduled(fixedRate = 10000)
    public void sendMorningNotifications() {

        userSubscriptionService.getAll().forEach(userSub -> {
            String notification = messageService.getNotification(userSub.getUser());
            if (notification != null) {
                notificationService.sendNotification(userSub, notification);
            }
        });

        System.out.println("The message was sent" + DateManager.now());
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
