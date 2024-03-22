package com.example.todoz.api0;

import com.example.todoz.week.Week;

import java.util.List;

public record WeekDTO(int number, List<TaskDTO> tasks) {

    public WeekDTO() {
        this(0, List.of());
    }

    public WeekDTO(Week w) {
        this(w.getWeekNumberNumber(), w.getTasks().stream().map(TaskDTO::new).toList());
    }
}
