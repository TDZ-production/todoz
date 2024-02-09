package com.example.todoz.services;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.TaskRepo;
import jakarta.transaction.Transactional;
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
        return taskRepo.findAllByUserIdAndDueDateBetweenOrderByDueDate(user.getId(), DateManager.getNextSunday(previousWeekNumber), DateManager.getNextSunday(currentWeekNumber));
    }

    public List<Task> findPlannedTasks(User user, Integer currentWeek) {
        return taskRepo.findAllByUserIdAndDueDateAfterOrderByDueDate(user.getId(), DateManager.getNextSunday(currentWeek));
    }

    public Map<Integer, Map<Integer, List<Task>>> sortTasksByYearAndWeek(List<Task> tasks) {
        Map<Integer, Map<Integer, List<Task>>> result = new HashMap<>();
        tasks.forEach(task -> {
                    Integer year = task.getDueDate().getYear();
                    Integer week = DateManager.getWeekNumber(task.getDueDate());
                    if (task.getDueDate().getMonthValue() == 12 && week == 1) {
                        week = week + Week.WEEKS_IN_YEAR;
                    }
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
        task.setDoneAt(done ? DateManager.now() : null);
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

    public List<Task> findLeftBehind(User user) {
        return taskRepo.findAllByUserIdAndLeftBehindNotNullOrderByLeftBehindDesc(user.getId());
    }

    public void reAdd(Long taskId, User user, Week week) {
        Task task = findTaskByIdAndUserId(taskId, user);
        task.setWeek(week);
        task.setLeftBehind(null);
        save(task);
    }

    public void leaveBehind(Long id, User user) {
        Task task = findTaskByIdAndUserId(id,user);
        task.setWeek(null);
        task.setLeftBehind(DateManager.now());
        save(task);
    }

    @Transactional
    public void deleteTask(Long id, User user) {
        taskRepo.deleteByIdAndUserId(id, user.getId());
    }
}


