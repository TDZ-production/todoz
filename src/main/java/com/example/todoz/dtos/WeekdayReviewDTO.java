package com.example.todoz.dtos;

import java.sql.Date;
import java.time.DayOfWeek;


public record WeekdayReviewDTO(DayOfWeek doneAt, Integer priority, Integer count) {
    public WeekdayReviewDTO(Integer priority, Date doneAt, Long count) {
        this(doneAt.toLocalDate().getDayOfWeek(), priority, count.intValue());
    }
}
