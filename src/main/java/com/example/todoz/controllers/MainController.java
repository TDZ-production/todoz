package com.example.todoz.controllers;

import com.example.todoz.models.Task;
import com.example.todoz.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    TaskService taskService;

    public MainController(TaskService taskService) {
        this.taskService = taskService;
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
}
