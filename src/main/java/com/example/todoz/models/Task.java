package com.example.todoz.models;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.services.DateManager;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private LocalDate leftBehind;
    private boolean done;
    @ManyToOne
    private Week week;
    @ManyToOne
    private User user;

    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public long getMaturity() {
        return ChronoUnit.DAYS.between(this.createdAt, DateManager.now());
    }

    // ☠️
    public LocalDate getDueDateDate() {
        if (dueDate != null) {
            return this.dueDate.toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * Formats dueDate to dd.MM.yyyy
     *
     * @return String dd.MM.yyyy
     */
    public String getDueDateFormat() {
        if (dueDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");
            return dueDate.format(formatter);
        } else {
            return null;
        }
    }

    /**
     * Gets day of the week
     *
     * @return Mon, Tue, Wed etc.
     */
    public String getDueDateDayOfWeek() {
        return this.dueDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public String getRemainingDays() {
        if (this.getDueDate() == null) {
            return null;
        } else {
            Duration duration = Duration.between(LocalDateTime.now().toLocalDate().atTime(23, 59, 59), this.getDueDate());
            if (duration.toDays() == 1) {
                return "Tomorrow";
            }
            else if (duration.toDays() == -1) {
                return "Yesterday";
            }
            else if (duration.toDays() == 0) {
                return "Today";
            }
            else if (duration.toDays() < -1) {
                return Math.abs(duration.toDays()) + " days ago";
            }
            else{
                return duration.toDays() + " days";
            }
        }
    }

    public Task merge(TaskUpdateDTO taskUpdate, Week currentWeek) {
        digestDueDate(taskUpdate.maybeDueDate(), currentWeek);
        this.priority = taskUpdate.priority();
        this.description = taskUpdate.description();

        return this;
    }

    public void digestDueDate(LocalDate dueDate, Week currentWeek) {
        if (dueDate == null) {
            setDueDate(null);
        } else {
            setDueDate(dueDate.atTime(23, 59, 59));
        }

        // if dueDate is in the future, set week to null
        if (dueDate != null && DateManager.getPrefixedWeek(dueDate) > DateManager.formattedCurrentWeek()) {
            setWeek(null);
        } else {
            setWeek(currentWeek);

        }
    }

    public Task copy(Week week) {
        Task task = new Task();
        task.setDescription(this.description);
        task.setUser(this.user);
        task.setPriority(this.priority);
        task.setWeek(week);
        return task;
    }
}
