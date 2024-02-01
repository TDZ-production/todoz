package com.example.todoz.services;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return taskRepo.findAllByUserIdAndDueDateWeekNumberGreaterThanAndDueDateWeekNumberLessThanEqualOrderByDueDate(user.getId(), previousWeekNumber, currentWeekNumber);
    }

    public List<Task> findLongTermTasks(User user, Integer currentWeek) {
        return taskRepo.findAllByUserIdAndDueDateWeekNumberGreaterThanOrderByDueDate(user.getId(), currentWeek);
    }

    public Map<Integer, Map<Integer, List<Task>>> sortTasksByYearAndWeek(List<Task> tasks) {
        Map<Integer, Map<Integer, List<Task>>> result = new HashMap<>();
        tasks.forEach(task -> {
                    Integer year = task.getDueDate().getYear();
                    Integer week = DateManager.getWeekNumber(task.getDueDate());
                    if (!result.containsKey(year)) {
                        result.put(year, new HashMap<>());
                    }
                    if (!result.get(year).containsKey(week)) {
                        result.get(year).put(week, new ArrayList<>());
                    }
                    result.get(year).get(week).add(task);
                });
        return result;
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

    public void update(Long taskId, TaskUpdateDTO taskUpdate, User user, Week currentWeek) {
        Task task = taskRepo.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new RuntimeException(String.format("Task not found with id: %d, %s", taskId, user)));

        taskRepo.save(task.merge(taskUpdate, currentWeek));
    }

    public List<Task> findLeftBehind(User user, Week week, Integer currentWeek) {
        return taskRepo.findAllByUserIdAndDoneIsFalseAndWeekIdLessThanOrWeekIdNullAndUserIdAndDoneIsFalseAndDueDateWeekNumberLessThanEqual(user.getId(), week.getId(), user.getId(), currentWeek);
    }
}


