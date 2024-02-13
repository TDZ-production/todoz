package com.example.todoz.dtos;

import java.sql.Date;
import java.time.DayOfWeek;


public record WeekdayReviewDTO(Integer priority, DayOfWeek doneAt, Integer count) {
    public WeekdayReviewDTO(Integer priority, Date doneAt, Long count) {
        this(priority, doneAt.toLocalDate().getDayOfWeek(), count.intValue());

    }
}
