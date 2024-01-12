package com.example.todoz.services;

import com.example.todoz.repos.NotificationRepo;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }
}
