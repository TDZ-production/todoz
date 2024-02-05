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

    @Scheduled(cron = "0 53 10 * * *")
    public void sendMorningNotifications() {

        userSubscriptionService.getAll().forEach(userSub -> {
            String notification = messageService.getNotification(userSub.getUser());
            if (notification != null) {
                notificationService.sendNotification(userSub, notification);
            }
        });

        System.out.println("The message was sent" + DateManager.now());
    }
}
