package com.example.todoz.services;

import com.example.todoz.dtos.NotificationDTO;
import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
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

    public Notification getNotification(User user, String timeSlot){
        int typeTask = getTypeTask();
        boolean notificationSingleTask = user.isNotificationSingleTask();
        List<Notification> notifications = null;
        Notification notification = null;

        /** No notifications for 0 tasks, it returns an empty task */
        if(typeTask == 3){
        } else if (typeTask == 2 && !notificationSingleTask){ /** type 2 it will always show one task */
            notifications = notificationRepo.
                    findAllByTimeSlotAndPussyMeterAndNotificationSingleTaskAndTypeTask(timeSlot, user.getPussyMeter(),true, getTypeTask());
        }else{
            notifications = notificationRepo.
                    findAllByTimeSlotAndPussyMeterAndNotificationSingleTaskAndTypeTask(timeSlot, user.getPussyMeter(), user.isNotificationSingleTask(), getTypeTask());
        }

        if(notifications != null){
            notification = getRandomNotification(notifications);
        }
        return notification;
    }
    public String getMorningNotification(User user) {

        return getJsonNotification(getTasks(), getNotification(user, "morning"),  user.isNotificationSingleTask());
    }

    public String getNoonNotification(User user) {

        return getJsonNotification(getTasks(), getNotification(user, "noon"),  user.isNotificationSingleTask());


    }

        public int getTypeTask() {
            Map<List<Task>, Integer> mapTaskAndTypeTask = getListTasksAndTypeTask();

            Integer typeTask = mapTaskAndTypeTask
                    .values()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (typeTask == null){
                throw new RuntimeException("no type task for the notification");
            }
            return typeTask;
        }

        public List<Task> getTasks() {
            Map<List<Task>, Integer> mapTaskAndTypeTask = getListTasksAndTypeTask();

            List<Task> tasks = mapTaskAndTypeTask
                    .keySet()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (tasks == null){
                throw new RuntimeException("no tasks");
            }
            return tasks;
        }


        public Map<List<Task>, Integer> getListTasksAndTypeTask() {
        List<Task> tasks = taskRepo.findAll();

        /** tasks for today */
        List<Task> tasksToday =tasks.stream().
                filter(t -> !t.isDone())
                .filter(t -> t.getRemainingDays() == "Today")
                .sorted(Comparator.comparing(Task::getPriority).reversed())
                .collect(Collectors.toList());

        /** tasks for today, tomorrow and yesterday */
        List<Task> filteredTasks = tasks.stream()
                .filter(t -> !t.isDone() &&
                        (("Today".equals(t.getRemainingDays())) ||
                                ("Yesterday".equals(t.getRemainingDays()) && t.getPriority() == 4)))
                .sorted(Comparator.comparing(Task::getPriority))
                .toList();


        if(tasksToday.size() > 5){
            return new HashMap<>(){{put(tasksToday, 1);}};
        }else if(!filteredTasks.isEmpty()){
            if(filteredTasks.size() >1 ){
                return new HashMap<>(){{put(filteredTasks, 1);}};
            }
            return new HashMap<>(){{put(filteredTasks, 2);}};
        }else{ //no tasks
            return new HashMap<>(){{put(filteredTasks, 3);}};

        }
    }

    public Notification getRandomNotification(List<Notification> notifications){
        Random random = new Random();
        return notifications.get(random.nextInt(notifications.size()));
    }


        public String getJsonNotification(List<Task> tasks, Notification notification, boolean notificationSingleTask){

            NotificationDTO notificationDTO = null;

            if(notification != null) {
                if (notificationSingleTask) {
                    notificationDTO = new NotificationDTO(
                            String.format(notification.getTitle(), tasks.size()),
                            String.format(notification.getDescription(), tasks.get(0).getDescription()));

                } else {
                    String threeTasksString = tasks.subList(0, 3).stream()
                            .map(task -> "- " + task.getDescription() + " \n")
                            .collect(Collectors.joining());


                    notificationDTO = new NotificationDTO(
                            String.format(notification.getTitle(), tasks.size()),
                            String.format(notification.getDescription(), threeTasksString));

                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(notificationDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}
