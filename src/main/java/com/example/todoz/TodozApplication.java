package com.example.todoz;

import com.example.todoz.models.Task;
import com.example.todoz.models.AppUser;
import com.example.todoz.models.TodoWeek;
import com.example.todoz.repos.AppUserRepo;
import com.example.todoz.repos.TaskRepo;
import com.example.todoz.repos.TodoWeekRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class    TodozApplication implements CommandLineRunner {

    private TaskRepo taskRepo;
    private AppUserRepo userRepo;
    private TodoWeekRepo todoWeekRepo;


    public TodozApplication(TaskRepo taskRepo, AppUserRepo userRepo, TodoWeekRepo todoWeekRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.todoWeekRepo = todoWeekRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodozApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AppUser appUser = new AppUser();
        userRepo.save(appUser);

        TodoWeek todoWeek = new TodoWeek();
        todoWeek.setAppUser(appUser);
        todoWeekRepo.save(todoWeek);

        Task task = new Task();
        task.setTodoWeek(todoWeek);
        taskRepo.save(task);
    }
}
