package com.example.todoz.task;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.week.Week;
import com.example.todoz.utility.DateManager;
import com.example.todoz.user.User;
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

    public static final int MAX_PRIORITY = 4;
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private LocalDateTime leftBehind;
    private LocalDateTime doneAt;
    @ManyToOne
    private Week week;
    @ManyToOne
    private User user;

    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getHour() {
        if (this.description != null && this.description.matches("^\\d{2}:\\d{2}.*")) {
            return Integer.parseInt(this.description.substring(0, 2));
        }
        return null;
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

    public long getDaysLeft() {
        if (this.dueDate == null) {
            return -1;
        }

        return Duration.between(LocalDateTime.now().toLocalDate().atTime(23, 59, 59), this.getDueDate()).toDays();
    }

    public String getRemainingDays() {
        if (this.getDueDate() == null) {
            return null;
        }

        long daysLeft = this.getDaysLeft();

        if (daysLeft == 1) {
            return "Tomorrow";
        }
        else if (daysLeft == -1) {
            return "Yesterday";
        }
        else if (daysLeft == 0) {
            return "Today";
        }
        else if (daysLeft < -1) {
            return Math.abs(daysLeft) + " days ago";
        }
        
        return daysLeft + " days";
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

    public boolean isDone() {
        return this.doneAt != null;
    }

    public Integer getDueDateWeekNumber() {
        return DateManager.getPrefixedWeek(this.dueDate);
    }
}
