package com.example.todoz.controllers;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.DateManager;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.UserService;
import com.example.todoz.services.WeekService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks/")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final WeekService weekService;

    @PostMapping("add")
    public String add(Task task, LocalDate maybeDueDate, Principal principal, RedirectAttributes ra) {
        task.digestDueDate(maybeDueDate, getWeek(principal));
        task.setUser(getUser(principal));
        taskService.save(task);
        if (task.getDueDate() != null && task.getDueDateWeekNumber() > DateManager.formattedCurrentWeek()) {
            ra.addFlashAttribute("longTermTask", true);
        }

        return "redirect:/";
    }

    @PostMapping("check/{id}")
    public String checkTask(@PathVariable Long id, @RequestParam boolean done, Principal principal) {
        taskService.checkedTask(id, getUser(principal), done);

        return "redirect:/";
    }

    @PostMapping("{id}")
    public String update(@PathVariable Long id, TaskUpdateDTO taskUpdate, Principal principal) {
        taskService.update(id, taskUpdate, getUser(principal), getWeek(principal));

        return "redirect:/";
    }

    @PostMapping("upcoming/{id}")
    public String upcomingUpdate(@PathVariable Long id, TaskUpdateDTO taskUpdate, Principal principal) {
        taskService.update(id, taskUpdate, getUser(principal), getWeek(principal));

        return "redirect:/longTerm";
    }

    @PostMapping("re-add/{id}")
    public String reAdd(@PathVariable Long id, Principal principal) {
        Task task = taskService.findTaskByIdAndUserId(id, getUser(principal));
        task.setWeek(getWeek(principal));
        taskService.save(task);

        return "redirect:/leftBehind";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.getCurrentWeek(getUser(principal));
    }
}
