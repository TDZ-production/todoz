package com.example.todoz.services;

import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.repos.NotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final TaskService taskService;

    public NotificationService(NotificationRepo notificationRepo, TaskService taskService){
        this.notificationRepo = notificationRepo;
        this.taskService = taskService;
    }


    public List<Notification> getAll(){
        return notificationRepo.findAll();
    }
    public void save(Notification n){
        notificationRepo.save(n);
    }
    public Notification getNotificationWithSameDay(Task task){
        Random r = new Random();
        if(task.getDueDate() == null){
            throw new RuntimeException("Inputted Task must have and DueDate assigned.");
        }else{
            Long taskDay = taskService.getLastingDays(task);
            Optional<Notification> notification = Optional.ofNullable(notificationRepo.findAllByLastingDays(taskDay).stream()
                    .peek(n -> n.setTitle(task.getDescription()))
                    .toList()
                    .get(r.nextInt(notificationRepo.findAll().size())));

            return notification.orElse(null);
        }
    }
}


