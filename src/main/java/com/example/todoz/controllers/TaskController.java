package com.example.todoz.controllers;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.utility.DateManager;
import com.example.todoz.task.Task;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import com.example.todoz.task.TaskService;
import com.example.todoz.user.UserService;
import com.example.todoz.week.WeekService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks/")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final WeekService weekService;

    @PostMapping("add")
    public String add(Task task, LocalDate maybeDueDate, Principal principal, RedirectAttributes ra) {
        User user = getUser(principal);
        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            task.digestDueDate(maybeDueDate, optWeek.get());
            task.setUser(user);
            taskService.save(task);
            if (task.getDueDate() != null && DateManager.getPrefixedWeek(task.getDueDate()) > DateManager.formattedCurrentWeek()) {
                ra.addFlashAttribute("plannedTask", user.getText("index_planned_popup"));
            }
        }

        return "redirect:/";
    }

    @PostMapping("check/{id}")
    public String checkTask(@PathVariable Long id, @RequestParam boolean done, Principal principal) {
        taskService.checkedTask(id, getUser(principal), done);

        return "redirect:/";
    }

    @PostMapping("{id}")
    public String update(@PathVariable Long id, TaskUpdateDTO taskUpdate, Principal principal, @RequestHeader String referer) throws URISyntaxException {
        taskService.update(id, taskUpdate, getUser(principal), getWeek(principal));
        String refererURI = new URI(referer).getPath();

        return "redirect:" + refererURI;
    }

    @PostMapping("re-add/{id}")
    public String reAdd(@PathVariable Long id, Principal principal) {
        taskService.reAdd(id, getUser(principal), getWeek(principal));

        return "redirect:/leftBehind";
    }

    @PostMapping("leaveBehind/{id}")
    public String leaveBehind(@PathVariable Long id, Principal principal, RedirectAttributes ra, @RequestHeader String referer) throws URISyntaxException {
        User user = getUser(principal);
        taskService.leaveBehind(id, user);
        String refererURI = new URI(referer).getPath();
        ra.addFlashAttribute("leftBehindTask", user.getText("index_left_behind_popup"));

        return "redirect:" + refererURI;
    }

    @PostMapping("delete/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal) {
        taskService.deleteTask(id, getUser(principal));

        return "redirect:/leftBehind";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.findOrCreateCurrentWeek(getUser(principal));
    }
}
