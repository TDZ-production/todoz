package com.example.todoz.task;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.dtos.WeekdayReviewDTO;
import com.example.todoz.utility.DateManager;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Task save(Task task) {
        if (task.getPriority() == null || task.getDescription() == null || task.getDescription().isBlank()) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400));
        }
       return taskRepo.save(task);
    }

    public List<Task> findUpcomingTasks(User user, Integer previousWeekNumber, Integer currentWeekNumber) {
        return taskRepo.findAllByUserIdAndDueDateBetweenAndLeftBehindNullOrderByDueDate(user.getId(), DateManager.getNextSunday(previousWeekNumber), DateManager.getNextSunday(currentWeekNumber));
    }

    public List<Task> findPlannedTasks(User user) {
        return taskRepo.findAllByUserIdAndDueDateAfterAndLeftBehindNullOrderByDueDate(user.getId(), DateManager.getNextSunday());
    }

    public Map<Integer, Map<Integer, List<Task>>> mapTasksByYearAndWeek(List<Task> tasks) {
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
        task.setLeftBehind(null);
        if (task.getDueDate() == null){
            task.setWeek(week);
        } else {
            task.setWeek(task.getDueDateWeekNumber() <= DateManager.formattedCurrentWeek() ? week : null);
        }
        save(task);
    }

    public void leaveBehind(Long id, User user) {
        Task task = findTaskByIdAndUserId(id, user);
        task.setWeek(null);
        task.setLeftBehind(DateManager.now());
        save(task);
    }

    @Transactional
    public void deleteTask(Long id, User user) {
        taskRepo.deleteByIdAndUserId(id, user.getId());
    }

    public List<List<Double>> getGraphData(User user) {
        List<WeekdayReviewDTO> review = taskRepo.getWeekdayReview(user.getId());
        List<List<Double>> result = new ArrayList<>();

        for (int day = 0; day < Week.DAYS_IN_WEEK; day++) {
            result.add(new ArrayList<>());
            for (int priority = 0; priority < Task.MAX_PRIORITY; priority++) {
                result.get(day).add(0.01);
            }
        }

        AtomicReference<Double> maxDivider = new AtomicReference<>(3.0);
        review.stream()
                .max(Comparator.comparing(WeekdayReviewDTO::count))
                .ifPresent(r -> {
                    if (r.count() > maxDivider.get()) maxDivider.set((double) r.count());
                });
        review.forEach(r -> result.get(r.doneAt().getValue() - 1).set(r.priority() - 1, r.count() / maxDivider.get()));

        // We start week with Sunday so move it from index 6 to 0
        result.add(0, result.get(6));
        result.remove(7);

        return result;
    }
}


