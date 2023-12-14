package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.Week;
import com.example.todoz.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findByPriority(Integer priority) {
        if (priority == 0 || priority > 4) {
            throw new RuntimeException("Input Integer must have value between 1 and 4.");
        } else {
            return taskRepo.findAllByPriority(priority).stream()
                    .filter(t -> t.getDueDate() == null)
                    .toList();
        }
    }

    public List<Task> findByPriorityWithDates(Integer priority) {
        if (priority == 0 || priority > 4) {
            throw new RuntimeException("Input Integer must have value between 1 and 4.");
        } else {
            return taskRepo.findAllByPriority(priority).stream()
                    .filter(t -> t.getDueDate() != null)
                    .toList();
        }
    }

    public List<Task> getAllAndSortByPriority() {
        return taskRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getPriority))
                .toList();
    }

    public void deleteById(Long id) {
        taskRepo.deleteById(id);
    }

    public void save(Task task) {
        taskRepo.save(task);
    }

    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    public Long getLastingDays(Task task) {
        if (task.getDueDate() == null) {
            throw new RuntimeException("Inputted Task must have and DueDate assigned.");
        } else {
            Duration duration = Duration.between(task.getCreatedAt(), task.getDueDate());
            return duration.toDays();
        }
    }

    public List<Task> findTasksForNextWeek() {
        return taskRepo.findAll()
                .stream()
                .filter(t -> t.getDueDateWeek() != null && t.getDueDateWeek() == Week.getCurrentWeekNumber() + 1)
                .collect(Collectors.toList());
    }

    public List<Task> findLongTerm() {
        return taskRepo.findAll()
                .stream()
                .filter(t -> t.getDueDateWeek() != null
                        && t.getDueDateWeek() > Week.getCurrentWeekNumber())
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }
}


