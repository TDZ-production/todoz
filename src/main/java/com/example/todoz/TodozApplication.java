package com.example.todoz;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.UserRepo;
import com.example.todoz.repos.TaskRepo;
import com.example.todoz.repos.WeekRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.format.TextStyle;
import java.util.Locale;

@SpringBootApplication
public class TodozApplication implements CommandLineRunner {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final WeekRepo weekRepo;


    public TodozApplication(TaskRepo taskRepo, UserRepo userRepo, WeekRepo weekRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.weekRepo = weekRepo;
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
        task.setDone(true);
        taskRepo.save(task);

//        Task task2 = new Task();
//        task2.setDescription("Task b");
//        task2.setWeek(week);
//        taskRepo.save(task2);
//
//        Task task3 = new Task();
//        task3.setDescription("Task c");
//        task3.setWeek(week);
//        task3.setDone(true);
//        taskRepo.save(task3);
    }
}
