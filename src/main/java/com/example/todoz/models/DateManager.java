package com.example.todoz.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;

public class DateManager {

    public static Integer formattedCurrentWeek() {
        LocalDate today = LocalDate.now();
        return (today.getYear() * 100) + today.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static Integer formatWeek(LocalDateTime date) {
        return (date.getYear() * 100) + date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static Integer formatWeek(LocalDate date) {
        return (date.getYear() * 100) + date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static Integer getYear(Integer week) {
        return week / 100;
    }

    public static Integer getWeekNumber(Integer week) {
        return week%100;
    }
}
