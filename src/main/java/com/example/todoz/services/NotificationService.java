package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Task;
import com.example.todoz.repos.NotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final TaskService taskService;


    public NotificationService(NotificationRepo notificationRepo, TaskService taskService) {
        this.notificationRepo = notificationRepo;
        this.taskService = taskService;
    }

//    public List<NotificationDTO> getMorningNotifications(){
//        List<Task> task = getTaskForToday();
//
//    }
//    public List<Task> getTaskForToday(){
//
//    }

//    public List<Task> getMoreTasks(){
//
//    }
//    public List<NotificationDTO> putTaskOnNotification(){
//
//    }


}
