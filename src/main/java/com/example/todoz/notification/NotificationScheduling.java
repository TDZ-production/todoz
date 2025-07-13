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

    @Scheduled(cron = "0 0 8 * * *")
    public void sendMorningNotifications() {

        userSubscriptionService.getAll().forEach(userSub -> {
            String notification = messageService.getNotification(userSub.getUser());
            if (notification != null) {
                notificationService.sendNotification(userSub, notification);
            }
        });
    }

    @Scheduled(cron = "0 */15 8-23 * * *")
    public void sendSpamNotifications() {
        // If it's 8:00, skip because morning notifications are already sent
        if (DateManager.now().getHour() == 8 && DateManager.now().getMinute() < 5) {
            return;
        }

        userSubscriptionService.getAll().forEach(userSub -> {
            String notification = messageService.getSpamNotification(userSub.getUser());
            if (notification != null) {
                notificationService.sendNotification(userSub, notification);
            }
        });
    }
}
