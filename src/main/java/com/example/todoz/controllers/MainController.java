package com.example.todoz.controllers;

import com.example.todoz.models.Task;
import com.example.todoz.models.Week;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.WeekService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Controller
public class MainController {

    TaskService taskService;

    WeekService weekService;

    public MainController(TaskService taskService, WeekService weekService) {
        this.taskService = taskService;
        this.weekService = weekService;
    }

    @GetMapping({"", "/"})
    public String showIndex(Model model) {
        model.addAttribute("currentWeek", weekService.findCurrentWeek());
        return "index";
    }

    @PostMapping("/add")
    public String add(Task task, LocalDate maybeDueDate) {

        if (maybeDueDate == null) {
            task.setWeek(weekService.findCurrentWeek());
        } else if (maybeDueDate.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear()) == Week.getCurrentWeekNumber()) {
            task.setWeek(weekService.findCurrentWeek());
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        } else {
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        }

        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("/weekReview")
    public String showWeekReview(Model model) {
        model.addAttribute("currentWeek", weekService.findCurrentWeek());
        model.addAttribute("donePercentage", weekService.findCurrentWeek().getDonePercentage());
        model.addAttribute("nextTasks", taskService.findTasksForNextWeek());
        return "weekReview";
    }

    @GetMapping("longTerm")
    public String showLongTerm(Model model) {
        model.addAttribute("longTerm", taskService.findLongTerm());
        return "longTerm";
    }
}
