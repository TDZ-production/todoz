package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "weeks")
public class Week {

    @Id
    @GeneratedValue
    private Long id;
    private Integer weekNumber;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "week")
    private List<Task> tasks;

    public Week() {
        this.weekNumber = LocalDate.now().get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());;
    }

    public static Integer getCurrentWeekNumber(){
        return LocalDate.now().get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public Long getDonePercentage() {
        long count = this.getTasks().stream().filter(Task::isDone).count();
        return Math.round((double) count / this.getTasks().size() * 100);
    }
}
