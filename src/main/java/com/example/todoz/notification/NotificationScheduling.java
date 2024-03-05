package com.example.todoz.notification;

import com.example.todoz.userSub.UserSubscriptionService;
import com.example.todoz.utility.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationScheduling {

    private final NotificationService notificationService;
    private final UserSubscriptionService userSubscriptionService;
    private final MessageService messageService;

    @Scheduled(cron = "0 30 8 * * *")
    public void sendMorningNotifications() {

        userSubscriptionService.getAll().forEach(userSub -> {
            String notification = messageService.getNotification(userSub.getUser());
            if (notification != null) {
                notificationService.sendNotification(userSub, notification);
            }
        });
    }
}
