package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "weeks", indexes = @Index(columnList = "weekNumber, user_id", unique = true))
public class Week {

    @Id
    @GeneratedValue
    private Long id;
    private Integer weekNumber;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "week")
    @OrderBy("done, dueDate ASC, priority DESC")
    private List<Task> tasks;

    public Week(User user) {
        this();
        this.user = user;
    }

    public Week() {
        this.weekNumber = DateManager.formattedCurrentWeek();
    }

    public Integer getWeekNumberNumber() {
         return this.weekNumber % 100;
    }

    public Long getDonePercentage() {
        long count = this.getTasks()
                .stream()
                .filter(Task::isDone)
                .count();
        return Math.round((double) count / this.getTasks().size() * 100);
    }

    public Long getNumberOfNotDoneTasks() {
        return this.tasks
                .stream()
                .filter(t -> !t.isDone())
                .count();
    }
}
