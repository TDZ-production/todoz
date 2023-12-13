package com.example.todoz.services;

import com.example.todoz.models.Week;
import com.example.todoz.repos.WeekRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeekService {

    WeekRepo weekRepo;

    public WeekService(WeekRepo weekRepo) {
        this.weekRepo = weekRepo;
    }

    public Week findCurrentWeek(){
        return weekRepo.findWeekWithHighestId();
    }
}
