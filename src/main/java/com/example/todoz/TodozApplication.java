package com.example.todoz;

import com.example.todoz.models.Notification;
import com.example.todoz.repos.TaskRepo;
import com.example.todoz.repos.WeekRepo;
import com.example.todoz.services.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;


@SpringBootApplication
public class TodozApplication implements CommandLineRunner {

    private final TaskRepo taskRepo;
    private final WeekRepo weekRepo;
    private final NotificationService notificationService;


    public TodozApplication(TaskRepo taskRepo, WeekRepo weekRepo, NotificationService notificationService) {
        this.taskRepo = taskRepo;
        this.weekRepo = weekRepo;
        this.notificationService = notificationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodozApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        notificationService.save(new Notification(2L, "I think you need to do this task mate...", null));
        notificationService.save(new Notification(2L, "Come on... you have only two days for this...", null));
        notificationService.save(new Notification(2L, "I dont want to call you lazy but ...", null));
        notificationService.save(new Notification(1L, "Can you already do it shithead!", null));
        notificationService.save(new Notification(1L, "Procrastinating huh...", null));
        notificationService.save(new Notification(1L, "Can you be less useless?", null));
        notificationService.save(new Notification(0L, "The number of remaining days is same as the number of bitches you have... ZERO!", null));
        notificationService.save(new Notification(0L, "Today is the day bitch!", null));
        notificationService.save(new Notification(0L, "Can you like... DO IT?!", null));
        notificationService.save(new Notification(null, "If you dont want to set the Deadline you better do it now!", null));
        notificationService.save(new Notification(null, "I like how you didnt specify the Deadline..so creative..", null));
        notificationService.save(new Notification(null, "No Deadline = NO BITCHES!", null));
    }
}
