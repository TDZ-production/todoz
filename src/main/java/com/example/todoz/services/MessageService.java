package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
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

            List<Task> tasks = week.getTasksForNotification();

            if (!tasks.isEmpty()) {

                List<Task> tasksToday = tasks.stream()
                        .filter(t -> t != null && t.getDueDate() != null && t.getDueDate().toLocalDate().equals(LocalDate.now()))
                        .toList();

                String title = "Wassup mf!";
                String body = getString(tasksToday, tasks);
                return String.format("{ \"title\": \"%s\", \"body\": \"%s\" }", title, body);
            }
        }
        return null;
    }

    private static String getString(List<Task> tasksToday, List<Task> tasks) {
        String body = "First task is: \\n";
        if (!tasksToday.isEmpty()) {
            body += tasks.get(0).getDescription() + "\\n" + tasksToday.size() + (tasksToday.size() == 1 ? " task is " : " tasks are ") + "due today";
        } else {
            body += tasks.get(0).getDescription();
        }
        return body;
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
