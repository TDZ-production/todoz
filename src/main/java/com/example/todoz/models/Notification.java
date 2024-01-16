package com.example.todoz.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;

    @Column(name = "pussy_meter")
    private int pussyMeter;

    @Column(name = "time_slot")
    private String timeSlot; //morning, noon or afternoon

    @Column(name = "type_tasks")
    private int typeTasks;
    /* 1 -> more than 1 task for today
       2 -> 1 task for today + pending and remaining tasks
       3 -> 0 task for today only pending and remaining tasks
       4 -> any type of tasks for today
     */
    private boolean morningNotificationSingleTask ; //true -> 1 task to put on the notification, false -> 3 task to put on the notification

    public Notification(String title, String description, int pussyMeter, String timeSlot, int typeTasks, boolean morningNotificationSingleTask) {
        this.title = title;
        this.description = description;
        this.pussyMeter = pussyMeter;
        this.timeSlot = timeSlot;
        this.typeTasks = typeTasks;
        this.morningNotificationSingleTask = morningNotificationSingleTask;
    }
}
