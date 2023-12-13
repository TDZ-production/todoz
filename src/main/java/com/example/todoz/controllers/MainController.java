package com.example.todoz.controllers;

import com.example.todoz.models.Task;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.WeekService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    TaskService taskService;

    WeekService weekService;

    public MainController(TaskService taskService, WeekService weekService) {
        this.taskService = taskService;
        this.weekService = weekService;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @PostMapping("/add")
    public String add(Task task) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/weekReview")
    public String showWeekReview(Model model){
        model.addAttribute("currentWeek", weekService.findCurrentWeek());
        return "weekReview";
    }
}
