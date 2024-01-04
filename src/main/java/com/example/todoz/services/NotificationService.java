package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.repos.NotificationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final TaskService taskService;

    public NotificationService(NotificationRepo notificationRepo, TaskService taskService) {
        this.notificationRepo = notificationRepo;
        this.taskService = taskService;
    }


    public List<Notification> getAll() {
        return notificationRepo.findAll();
    }

    public void save(Notification n) {
        notificationRepo.save(n);
    }


    public Notification getNotificationById(Long id) {
        return notificationRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public NotificationDTO getNotificationDTO(User user, String type) {

        List<String> tasksDescriptions = taskService.findNotDoneTasksByUser(user).stream()
                .map(Task::getDescription)
                .toList();
        List<Notification> notifications = notificationRepo.findAllByTypeAndPussyMeter(type, user.getPussyMeter());
        String title = notifications.get(new Random().nextInt(notifications.size())).getTitle();

        return new NotificationDTO(title, tasksDescriptions);
    }
}


