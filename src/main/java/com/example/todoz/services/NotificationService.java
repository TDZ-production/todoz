package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.NotificationRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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

    public String getMorningNotification(User user) throws JsonProcessingException {
        int pussyMeter = user.getPussyMeter();
        boolean notificationSingleTask = user.isMorningNotificationSingleTask();
        List<Notification> notifications = notificationRepo.findAllByTimeSlotAndPussyMeterAndMorningNotificationSingleTask("morning", pussyMeter, notificationSingleTask);
        List<Task> tasks = getTasksForToday();

        Notification notification = getRandomNotification(notifications);

        return getJsonNotificationOneTask(tasks, notification,  notificationSingleTask);
    }

    public List<Task> getTasksForToday(){
        Week week = new Week();
        week.getSortedTasks().forEach(System.out::println);
        return week.getSortedTasks();
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
                    String.format(notification.getDescription(), tasks.get(0)));

        }else{
            notificationDTO = new NotificationDTO(
                    String.format(notification.getTitle(), tasks.size()),
                    String.format(notification.getDescription(), tasks.subList(0, 2)));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(notificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
