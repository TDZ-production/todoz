package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final WeekService weekService;
    private final NotificationRepo notificationRepo;

//
//        public Notification getNotification(User user, String timeSlot) {
//        return getRandomNotification(notificationRepo.
//                findAllByTimeSlotAndPussyMeterAndNotificationSingleTaskAndTypeTask(
//                        timeSlot,
//                        user.getPussyMeter(),
//                        true,
//                        getTypeTask(user)));
//
//    }

//
//    public Notification getNotification(User user, String timeSlot) {
//        return getRandomNotification(notificationRepo.
//                findAllByTimeSlotAndPussyMeterAndNotificationSingleTaskAndTypeTask(
//                        timeSlot,
//                        user.getPussyMeter(),
//                        true,
//                        getTypeTask(user)));
//
//    }
//
//    public String getMorningNotification(User user) {
//        return getJsonNotification(getTasks(user), getNotification(user, "morning"));
//    }
//
//
//    public int getTypeTask(User user) {
//        List<Task> tasksToday = getTasks(user);
//
//        if (tasksToday.size() > 1) {
//            return 1;
//        } else if (tasksToday.size() == 1) {
//            return 2;
//        } else {
//            return 3;
//        }
//    }

//    public Notification getRandomNotification(List<Notification> notifications) {
//        Random random = new Random();
//        return notifications.get(random.nextInt(notifications.size()));
//    }

    public String getNotification(User user) {

        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            Week week = optWeek.get();
            week.getTasks().size();

            List<Task> tasks = week.getTasksForNotification();

            if (!tasks.isEmpty()) {

                var json = """
                        {
                          "title": "Wassup mf!",
                          "body": "First task is:
                           %s"
                        }
                        """;
                return String.format(json, tasks.get(0));
            }
        }
        return null;
    }


//    public String getJsonNotification(List<Task> tasks, Notification notification) {
//
//        NotificationDTO notificationDTO = null;
//
//        if (notification != null && !tasks.isEmpty()) {
//            notificationDTO = new NotificationDTO(
//                    String.format(notification.getTitle(), tasks.size()),
//                    String.format(notification.getDescription(), tasks.get(0).getDescription()));
//
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.writeValueAsString(notificationDTO);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
