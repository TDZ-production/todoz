package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.repos.NotificationRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class MessageService {

    private final NotificationRepo notificationRepo;


    public MessageService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public Notification getNotification(User user, String timeSlot) {
        return getRandomNotification(notificationRepo.
                findAllByTimeSlotAndPussyMeterAndNotificationSingleTaskAndTypeTask(
                        timeSlot,
                        user.getPussyMeter(),
                        true,
                        getTypeTask(user)));

    }

    public String getMorningNotification(User user) {
        return getJsonNotification(getTasks(user), getNotification(user, "morning"));
    }


    public int getTypeTask(User user) {
        List<Task> tasksToday = getTasks(user);

        if (tasksToday.size() > 1) {
            return 1;
        } else if (tasksToday.size() == 1) {
            return 2;
        } else {
            return 3;
        }
    }

    public List<Task> getTasks(User user) {
        return user.getCurrentTasks();
    }

    public Notification getRandomNotification(List<Notification> notifications) {
        Random random = new Random();
        return notifications.get(random.nextInt(notifications.size()));
    }


    public String getJsonNotification(List<Task> tasks, Notification notification) {

        NotificationDTO notificationDTO = null;

        if (notification != null && !tasks.isEmpty()) {
            notificationDTO = new NotificationDTO(
                    String.format(notification.getTitle(), tasks.size()),
                    String.format(notification.getDescription(), tasks.get(0).getDescription()));

        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(notificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
