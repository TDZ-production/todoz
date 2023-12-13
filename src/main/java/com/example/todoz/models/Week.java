package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
        Calendar calendar = Calendar.getInstance();
        this.weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public Long getDonePercentage() {
        long count = this.getTasks().stream().filter(Task::isDone).count();
        return Math.round((double) count / this.getTasks().size() * 100);
    }
}
