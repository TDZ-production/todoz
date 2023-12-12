package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> filterByRating(Integer ratingNumber){
        if(ratingNumber == 0 || ratingNumber > 4){
            throw new RuntimeException("Input Integer must have value between 1 and 4.");
        } else {
            return taskRepo.findAllByPriority(ratingNumber);
        }
    }
}
