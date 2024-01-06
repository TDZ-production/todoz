package com.example.todoz.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

public class DateManager {

    /**
     * Formats current day to YYYYww
     *
     * @return YYYYww
     */
    public static Integer formattedCurrentWeek() {
        return getYearOfNextOrSameSaturday(LocalDate.now()) * 100 + getWeekNumberOfNextOrSameSaturday(LocalDate.now());
    }

    /**
     * Formats a day to YYYYww
     *
     * @return YYYYww
     */
    public static Integer formatWeek(TemporalAccessor date) {
        return getYearOfNextOrSameSaturday(date) * 100 + getWeekNumberOfNextOrSameSaturday(date);
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

    /**
     * Gets week number of the nearest Saturday
     *
     * @param date Date
     * @return Week number as Integer
     */
    public static Integer getWeekNumberOfNextOrSameSaturday(TemporalAccessor date) {
        return date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }
}
