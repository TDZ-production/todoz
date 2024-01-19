package com.example.todoz.models;

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

    private int pussyMeter;

    /**morning, noon or afternoon */
    private String timeSlot;

    /** 1 -> You have tasks for today
     *  2- > You have only one task today
        3 -> 0 tasks for today
 */
    private int typeTask;

    /** true -> 1 task to put on the notification, false -> 3 task to put on the notification */
    private boolean notificationSingleTask;

}
