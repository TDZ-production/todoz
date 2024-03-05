package com.example.todoz.services;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

@Service
public class DateManager {

    /**
     * Formats a day to YYYYww
     *
     * @return YYYYww
     */
    public static Integer getPrefixedWeek(TemporalAccessor date) {
        return getYearOfNextOrSameSaturday(date) * 100 + date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static Integer getWeekNumber(TemporalAccessor date) {
        return date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static Integer formattedCurrentWeek() {
        return getPrefixedWeek(now());
    }

    /**
     * Gets year of the nearest Saturday
     *
     * @param date Date
     * @return Year as Integer
     */
    public static Integer getYearOfNextOrSameSaturday(TemporalAccessor date) {
        LocalDate localDate = LocalDate.from(date);
        return localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).getYear();
    }

    public static LocalDateTime getNextSunday(Integer prefixedWeekNumber) {
        return LocalDateTime.now()
                .with(WeekFields.SUNDAY_START.weekOfWeekBasedYear(), prefixedWeekNumber % 100)
                .with(TemporalAdjusters.nextOrSame((DayOfWeek.SUNDAY)));
    }
}
