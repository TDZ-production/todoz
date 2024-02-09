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

import java.net.URI;
import java.net.URISyntaxException;
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
            ra.addFlashAttribute("plannedTask", true);
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

    @PostMapping("planned/{id}")
    public String plannedUpdate(@PathVariable Long id, TaskUpdateDTO taskUpdate, Principal principal) {
        taskService.update(id, taskUpdate, getUser(principal), getWeek(principal));

        return "redirect:/planned";
    }

    @PostMapping("re-add/{id}")
    public String reAdd(@PathVariable Long id, Principal principal) {
        taskService.reAdd(id, getUser(principal), getWeek(principal));

        return "redirect:/leftBehind";
    }

    @PostMapping("leaveBehind/{id}")
    public String leaveBehind(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        taskService.leaveBehind(id, getUser(principal));
        ra.addFlashAttribute("leftBehindTask", true);

        return "redirect:/";
    }

    @PostMapping("delete/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal, @RequestHeader String referer) throws URISyntaxException {
        taskService.deleteTask(id, getUser(principal));
        String refererURI = new URI(referer).getPath();

        return "redirect:" + refererURI;
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.getCurrentWeek(getUser(principal));
    }
}
