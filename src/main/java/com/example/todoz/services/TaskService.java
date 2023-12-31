package com.example.todoz.services;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public void save(Task task) {
        taskRepo.save(task);
    }

    public List<Task> findUpcomingTasks(User user, Integer previousWeekNumber, Integer currentWeekNumber) {
        return taskRepo.findAllByUserIdAndDueDateWeekNumberGreaterThanAndDueDateWeekNumberLessThanEqual(user.getId(), previousWeekNumber, currentWeekNumber);
    }

    public List<Task> findLongTermTasks(User user, Integer currentWeek) {
        return taskRepo.findAllByUserIdAndDueDateWeekNumberGreaterThan(user.getId(), currentWeek);
    }

    public void checkedTask(Long taskId, User user, boolean done) {
        Task task = taskRepo.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException(String.format("Task not found with id: %d, %s", taskId, user)));
        task.setDone(done);
        taskRepo.save(task);
    }

    public Task findTaskByIdAndUserId(Long taskId, User user) {
        return taskRepo.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException(String.format("Task not found with id: %d, %s", taskId, user)));
    }

    public Task update(Long taskId, TaskUpdateDTO taskUpdate, User user, Week currentWeek) {
        Task task = taskRepo.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException(String.format("Task not found with id: %d, %s", taskId, user)));

        return taskRepo.save(task.merge(taskUpdate, currentWeek));
    }
}


