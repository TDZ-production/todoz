package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.NotificationRepo;
import com.example.todoz.repos.TaskRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final TaskRepo taskRepo;


    public NotificationService(NotificationRepo notificationRepo, TaskRepo taskRepo) {
        this.notificationRepo = notificationRepo;
        this.taskRepo = taskRepo;
    }

    public String getMorningNotification(User user) {
        int pussyMeter = user.getPussyMeter();
        boolean notificationSingleTask = user.isMorningNotificationSingleTask();
        List<Notification> notifications = notificationRepo.findAllByTimeSlotAndPussyMeterAndMorningNotificationSingleTask("morning", pussyMeter, notificationSingleTask);
        List<Task> tasks = getTasksForToday();

        Notification notification = getRandomNotification(notifications);

        return getJsonNotificationOneTask(tasks, notification,  notificationSingleTask);
    }


    public List<Task> getTasksForToday() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepo.findAll();

        tasks.sort(
                Comparator.comparing(Task::isDone).reversed()
                        .thenComparing(Task::getPriority).reversed()
                        .thenComparing(t -> t.getDueDate() == null ? now.plusDays(1) : t.getDueDate()).reversed()
                        .thenComparing(Task::getId).reversed()
        );

        return tasks;
    }

    public Notification getRandomNotification(List<Notification> notifications){
        Random random = new Random();
        return notifications.get(random.nextInt(notifications.size()));
    }

//    public List<Task> getMoreTasks(){
//
//    }


    public String getJsonNotificationOneTask(List<Task> tasks, Notification notification, boolean notificationSingleTask){

        NotificationDTO notificationDTO;
        if(notificationSingleTask){
            notificationDTO = new NotificationDTO(
                    String.format(notification.getTitle(), tasks.size()),
                    String.format(notification.getDescription(), tasks.get(0).getDescription()));

        }else{
            notificationDTO = new NotificationDTO(
                    String.format(notification.getTitle(), tasks.size()),
                    String.format(notification.getDescription(), tasks.subList(0, 2).stream().map(task -> task.getDescription())));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(notificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
