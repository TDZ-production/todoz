package com.example.todoz.services;

import com.example.todoz.models.Notification;
import com.example.todoz.repos.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class NotificationService {

    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo){
        this.notificationRepo = notificationRepo;
    }


    public List<Notification> getAll(){
        return notificationRepo.findAll();
    }
    public void save(Notification n){
        notificationRepo.save(n);
    }
}


