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


import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final TaskRepo taskRepo;


    public NotificationService(NotificationRepo notificationRepo, TaskRepo taskRepo) {
        this.notificationRepo = notificationRepo;
        this.taskRepo = taskRepo;
    }

    public String getMorningNotification(User user) {
        Map<List<Task>, Integer> mapTaskAndTypeTask = getTasks();

        List<Task> tasks = mapTaskAndTypeTask
                .keySet()
                .stream()
                .findFirst()
                .orElse(null);

        Integer typeTask = mapTaskAndTypeTask
                .values()
                .stream()
                .findFirst()
                .orElse(null);

        if (tasks == null || typeTask == null){
            throw new RuntimeException("no tasks or no type tasks");
        }

        int pussyMeter = user.getPussyMeter();
        boolean notificationSingleTask = user.isMorningNotificationSingleTask();

        List<Notification> notifications = notificationRepo.
                findAllByTimeSlotAndPussyMeterAndMorningNotificationSingleTaskAndTypeTask("morning", pussyMeter, notificationSingleTask, typeTask);

        if(notifications.isEmpty()){
            throw new RuntimeException("There is no notification for that case");
        }

        Notification notification = getRandomNotification(notifications);

        return getJsonNotificationOneTask(tasks, notification,  notificationSingleTask);
    }


    public Map<List<Task>, Integer> getTasks() {
        List<Task> tasks = taskRepo.findAll();

        //tasks for today
        List<Task> tasksToday =tasks.stream().
                filter(t -> !t.isDone())
                .filter(t -> t.getRemainingDays() == "Today")
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .collect(Collectors.toList());

        //tasks for today, tomorrow and yesterday
        List<Task> filteredTasks = tasks.stream()
                .filter(t -> !t.isDone() &&
                        (("Today".equals(t.getRemainingDays())) ||
                                (("Yesterday".equals(t.getRemainingDays()) ||  "Tomorrow".equals(t.getRemainingDays())) && t.getPriority() == 4)))
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .toList();


        if(tasksToday.size() > 3){
            return new HashMap<>(){{put(tasksToday, 1);}};
        } else if (!tasksToday.isEmpty()) {
            return new HashMap<>(){{put(filteredTasks, 2);}};
        }else if( filteredTasks.size() > 1){
            return new HashMap<>(){{put(filteredTasks, 3);}};
        }else{ //no tasks
            return new HashMap<>(){{put(filteredTasks, 4);}};

        }
    }

    public Notification getRandomNotification(List<Notification> notifications){
        Random random = new Random();
        return notifications.get(random.nextInt(notifications.size()));
    }


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
