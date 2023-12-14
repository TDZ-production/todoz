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
    public List<Notification> getNotificationWithSameDay(Task task){
        Random r = new Random();
        if(task == null){
            throw new RuntimeException("Inputted Task must not be null.");
        } else if (task.getDueDate() == null){
            throw new RuntimeException("Inputted Task must have and DueDate assigned.");
        } else{
            Long taskDay = taskService.geRemainingDays(task);
            return  notificationRepo.findAllByRemainingDays(taskDay).stream()
                    .peek(n -> n.setDescription(task.getDescription()))
                    .toList();
//                    .get(r.nextInt(notificationRepo.findAllByLastingDays(taskDay).size())));

        }
    }


    public Notification getNotificationById(Long id){
        return notificationRepo.findById(id).orElseThrow(RuntimeException::new);
    }


}


