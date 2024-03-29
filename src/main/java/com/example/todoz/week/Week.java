package com.example.todoz.week;

import com.example.todoz.utility.WeekQuality;
import com.example.todoz.utility.DateManager;
import com.example.todoz.task.Task;
import com.example.todoz.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "weeks", indexes = @Index(columnList = "weekNumber, user_id", unique = true))
public class Week {
    public static final int DAYS_IN_WEEK = 7;
    @Id
    @GeneratedValue
    private Long id;
    private Integer weekNumber;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "week")
    //@OrderBy("done, priority DESC, dueDate ASC")
    private List<Task> tasks = new ArrayList<>();

    public final static int WEEKS_IN_YEAR = 52;

    public Week(User user) {
        this();
        this.user = user;
    }

    public Week() {
        this.weekNumber = DateManager.formattedCurrentWeek();
    }

    public List<Task> getSortedTasks() {
        LocalDateTime now = LocalDateTime.now();

        tasks.sort(
                Comparator.comparing(Task::isDone)
                        .thenComparing(t -> t.getDueDate() == null ? now.plusDays(1) : t.getDueDate()).reversed()
                        .thenComparing(Task::getPriority)
                        .thenComparing(Task::getId).reversed()
        );

        return tasks;
    }

    public List<Task> getTasksForNotification() {
        return getSortedTasks().stream()
                .filter(t -> !t.isDone())
                .filter(t -> t.getDueDate() == null || t.getDueDate().toLocalDate().equals(LocalDate.now()))
                .toList();
    }

    public int getWeekNumberNumber() {
        return this.weekNumber % 100;
    }

    public WeekQuality getWeekQuality() {
        long doneCount = this.getDoneCount();
        long donePercentage = this.getDonePercentage();

        if (doneCount > 9) {
            return WeekQuality.ACTIVE;
        } else if (doneCount < 3 && donePercentage < 30) {
            return WeekQuality.LAZY;
        } else {
            return WeekQuality.NEUTRAL;
        }
    }

    public long getDonePercentage() {
        long count = this.getDoneCount();

        return Math.round((double) count / this.getTasks().size() * 100);
    }

    public long getDoneTasksByPriority(Integer priority) {
        return getTasksByPriority(priority).stream()
                .filter(Task::isDone)
                .count();
    }

    public List<Task> getTasksByPriority(Integer priority) {
        return this.getTasks()
                .stream()
                .filter(t -> t.getPriority().equals(priority))
                .toList();
    }

    public long getDoneCount() {
        return this.getTasks()
                .stream()
                .filter(Task::isDone)
                .count();
    }
}
