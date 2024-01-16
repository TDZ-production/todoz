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
    private List<Task> tasks;

    private final static int WEEKS_IN_YEAR = 52;

    public Week(User user) {
        this();
        this.user = user;
    }

    public Week() {
        this.weekNumber = DateManager.formattedCurrentWeek();
    }

    public List<Task> getSortedTasks() {
        tasks.sort(
                Comparator.comparing(Task::isDone).reversed()
                        .thenComparing(Task::getPriority).reversed()
                        .thenComparing(t -> t.getDueDate() == null ? LocalDateTime.now().plusDays(1) : t.getDueDate())
        );
        return tasks;
    }

    public int nextWeekNumber() {
        return (getWeekNumberNumber() % WEEKS_IN_YEAR) + 1;
    }
    public int getWeekNumberNumber() {
         return this.weekNumber % 100;
    }

    public Long getDonePercentage() {
        long count = this.getTasks()
                .stream()
                .filter(Task::isDone)
                .count();
        return Math.round((double) count / this.getTasks().size() * 100);
    }
}
