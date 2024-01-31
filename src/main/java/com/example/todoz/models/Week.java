package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "weeks", indexes = @Index(columnList = "weekNumber, user_id", unique = true))
public class Week {
    @Id @GeneratedValue
    private Long id;
    private Integer weekNumber;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "week")
    //@OrderBy("done, priority DESC, dueDate ASC")
    private List<Task> tasks = List.of();

    private final static int WEEKS_IN_YEAR = 52;

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
                Comparator.comparing(Task::isDone).reversed()
                        .thenComparing(Task::getPriority).reversed()
                        .thenComparing(t -> t.getDueDate() == null ? now.plusDays(1) : t.getDueDate()).reversed()
                        .thenComparing(Task::getId).reversed()
        );

        return tasks;
    }

    public int nextWeekNumber() {
        return (getWeekNumberNumber() % WEEKS_IN_YEAR) + 1;
    }

    public int getWeekNumberNumber() {
         return this.weekNumber % 100;
    }

    public String getCatImage() {
        WeekQuality quality = this.getWeekQuality();

        return switch (quality) {
            case ACTIVE -> "/img/cat_pm_1.png";
            case LAZY -> "/img/cat_pm_2.png";
            default -> "/img/cat_pm_0.png";
        };
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

    public long getDoneCount() {
        return this.getTasks()
                .stream()
                .filter(Task::isDone)
                .count();
    }
}
