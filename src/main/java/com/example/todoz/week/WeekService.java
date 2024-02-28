package com.example.todoz.week;

import com.example.todoz.utility.DateManager;
import com.example.todoz.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeekService {
    private final WeekRepo weekRepo;

    public WeekService(WeekRepo weekRepo) {
        this.weekRepo = weekRepo;
    }

    public Optional<Week> findCurrentWeek(User user){
        return weekRepo.findByWeekNumberAndUserId(DateManager.formattedCurrentWeek(), user.getId());
    }

    public Week findOrCreateCurrentWeek(User user) {
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
}
