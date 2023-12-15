package com.example.todoz.services;

import com.example.todoz.models.Week;
import com.example.todoz.repos.WeekRepo;
import org.springframework.stereotype.Service;

@Service
public class WeekService {
    private final TaskService taskService;
    private final WeekRepo weekRepo;

    public WeekService(TaskService taskService, WeekRepo weekRepo) {
        this.taskService = taskService;
        this.weekRepo = weekRepo;
    }

    public Week findCurrentWeek(){
        return weekRepo.findByWeekNumber(Week.getCurrentWeekNumber());
    }

    public void save(Week week) {
        weekRepo.save(week);
    }

//    public Long getDonePercentage() {
//        Week week = weekRepo.findWeekWithHighestId();
//        long count = week.getTasks().stream().filter(Task::isDone).count();
//        return Math.round((double) count / week.getTasks().size() * 100);
//    }
}
