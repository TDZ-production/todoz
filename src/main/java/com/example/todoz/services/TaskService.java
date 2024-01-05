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

    public List<Task> findTasksForThisWeek(User user) {
        return taskRepo.findByUserId(user.getId())
                .stream()
                .filter(Task::isUpcoming)
                .toList();
    }

    public List<Task> findLongTermTasks(User user) {
        return taskRepo.findByUserId(user.getId())
                .stream()
                .filter(Task::isLongTerm)
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();
    }

    public void checkedTask(Long id, boolean done) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setDone(done);
        taskRepo.save(task);
    }

    public Task update(Long id, TaskUpdateDTO taskUpdate, User user, Week currentWeek) {
        Task task = taskRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException(String.format("Task not found with id: %d, %s", id, user)));

        return taskRepo.save(task.merge(taskUpdate, currentWeek));
    }
}


