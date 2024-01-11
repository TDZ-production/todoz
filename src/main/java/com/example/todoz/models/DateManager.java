package com.example.todoz.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

public class DateManager {

    /**
     * Formats a day to YYYYww
     *
     * @return YYYYww
     */
    public static Integer formatWeek(TemporalAccessor date) {
        return getYearOfNextOrSameSaturday(date) * 100 + date.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public static Integer formattedCurrentWeek() {
        return formatWeek(LocalDate.now());
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
}
