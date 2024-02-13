package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.repos.WeekRepo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class WeekService {
    private final WeekRepo weekRepo;

    public WeekService(WeekRepo weekRepo) {
        this.weekRepo = weekRepo;
    }

    public Optional<Week> findCurrentWeek(User user){
        return weekRepo.findByWeekNumberAndUserId(DateManager.formattedCurrentWeek(), user.getId());
    }

    public Week getCurrentWeek(User user) {
        var optWeek = findCurrentWeek(user);

        if (optWeek.isPresent()) {
            return optWeek.get();
        }

        Week w = new Week(user);

        return save(w);
    }

    public Week save(Week week) {
        return weekRepo.save(week);
    }

    public Optional<Week> findPreviousWeek(User user) {
        return weekRepo.findTopByUserIdOrderByWeekNumberDesc(user.getId());
    }

    public Map<DayOfWeek, List<Task>> mapDoneTasksByDayOfWeek(List<Task> tasks) {
        Map<DayOfWeek, List<Task>> result = new HashMap();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            result.put(dayOfWeek, new ArrayList<>());
        }
        tasks.forEach(task -> result.get(task.getDoneAt().getDayOfWeek()).add(task));

        return result;
    }
}
