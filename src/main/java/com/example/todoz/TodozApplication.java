package com.example.todoz;

import com.example.todoz.models.Notification;
import com.example.todoz.models.Task;
import com.example.todoz.models.Week;
import com.example.todoz.repos.NotificationRepo;
import com.example.todoz.repos.UserRepo;
import com.example.todoz.repos.TaskRepo;
import com.example.todoz.repos.WeekRepo;
import com.example.todoz.services.NotificationService;
import com.example.todoz.services.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class TodozApplication implements CommandLineRunner {

    private final TaskRepo taskRepo;
    private final TaskService taskService;
    private final UserRepo userRepo;
    private final WeekRepo weekRepo;
    private final NotificationService notificationService;
    private final NotificationRepo notificationRepo;


    public TodozApplication(TaskRepo taskRepo, TaskService taskService, UserRepo userRepo, WeekRepo weekRepo, NotificationService notificationService, NotificationRepo notificationRepo) {
        this.taskRepo = taskRepo;
        this.taskService = taskService;
        this.userRepo = userRepo;
        this.weekRepo = weekRepo;
        this.notificationService = notificationService;
        this.notificationRepo = notificationRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodozApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Week week = new Week();
        weekRepo.save(week);

        Task task = new Task();
        task.setDescription("Task a");
        task.setWeek(week);
        task.setDone(false);
        task.setPriority(1);
        task.setDueDate(LocalDateTime.of(2023, 12, 16, 23, 59, 59));
        taskRepo.save(task);

        Task task2 = new Task();
        task2.setDescription("Task b");
        task2.setWeek(week);
        task2.setDone(false);
        task2.setPriority(1);
        task2.setDueDate(LocalDateTime.of(2023, 12, 15, 23, 59, 59));
        taskRepo.save(task2);

        Task task3 = new Task();
        task3.setDescription("Task c");
        task3.setWeek(week);
        task3.setDone(false);
        task3.setPriority(1);
        task3.setDueDate(LocalDateTime.of(2023, 12, 14, 23, 59, 59));
        taskRepo.save(task3);

        notificationService.save(new Notification(2L, "I think you need to do this task mate...", null));
        notificationService.save(new Notification(2L, "Come on... you have only two days for this...", null));
        notificationService.save(new Notification(2L, "I dont want to call you lazy but ...", null));
        notificationService.save(new Notification(1L, "Can you already do it shithead!", null));
        notificationService.save(new Notification(1L, "Procrastinating huh...", null));
        notificationService.save(new Notification(1L, "Can you be less useless?", null));
        notificationService.save(new Notification(0L, "The number of lasting days is same as the number of bitches you have... ZERO!", null));
        notificationService.save(new Notification(0L, "Today is the day bitch!", null));
        notificationService.save(new Notification(0L, "Can you like... DO IT?!", null));

        System.out.println(notificationService.getNotificationWithSameDay(taskService.getAllAndSortByPriority().stream().findFirst().orElse(null)));
//        notificationRepo.findAllByLastingDays(1L).forEach(n -> System.out.println(n.getTitle()));
//        taskService.getAllAndSortByPriority().forEach(t-> System.out.println(t.getPriority()));
    }
}
