package com.example.todoz.dtos;

import java.math.BigInteger;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record WeekdayReviewDTO(Date doneAt, Integer priority, BigInteger count) {
}

//public record WeekdayReviewDTO(DayOfWeek doneAt, Integer priority, Integer count) {
////    public WeekdayReviewDTO(LocalDateTime doneAt, Integer priority, Long count) {
////        this(doneAt.getDayOfWeek(), priority, count.intValue());
////    }
