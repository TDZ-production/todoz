package com.example.todoz.controllers;

import com.example.todoz.models.*;
import com.example.todoz.services.DateManager;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.UserService;
import com.example.todoz.services.WeekService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final TaskService taskService;
    private final WeekService weekService;

    @GetMapping
    public String showIndex(Model model, Principal principal) {
        Optional<Week> currentWeek = weekService.findCurrentWeek(getUser(principal));
        Optional<Week> optPreviousWeek = weekService.findPreviousWeek(getUser(principal));

        if (currentWeek.isEmpty() && optPreviousWeek.isPresent()) {
            Week previousWeek = optPreviousWeek.get();
            List<Task> upcomingTasks = taskService
                    .findUpcomingTasks(getUser(principal), previousWeek.getWeekNumber(), DateManager.formattedCurrentWeek());

            model.addAttribute("user", getUser(principal));
            model.addAttribute("previousWeek", previousWeek);
            model.addAttribute("upcomingTasks", upcomingTasks);

            return "weekReview";
        } else if (currentWeek.isEmpty()) {
            Week week = new Week(getUser(principal));
            weekService.save(week);

            model.addAttribute("currentWeek", week);
        } else {
            model.addAttribute("currentWeek", currentWeek.get());
        }

        model.addAttribute("user", getUser(principal));

        return "index";
    }

    @PostMapping("/startNewWeek")
    public String startNewWeek(Principal principal, @RequestParam(value = "taskIds", required = false) List<Long> taskIds, @RequestParam(value = "leftBehinds", required = false) List<Long> leftBehinds) {
        Week week = new Week(getUser(principal));
        weekService.save(week);

        if (taskIds != null && !taskIds.isEmpty()) {
            taskIds.stream()
                    .map(taskId -> taskService.findTaskByIdAndUserId(taskId, getUser(principal)))
                    .forEach(task -> {
                        if (task.isDone()) {
                            taskService.save(task.copy(week));
                        } else {
                            task.setWeek(week);
                            weekService.save(week);
                        }
                    });
        }

        if (leftBehinds != null && !leftBehinds.isEmpty()) {
            leftBehinds.stream()
                    .map(leftID -> taskService.findTaskByIdAndUserId(leftID, getUser(principal)))
                    .filter(t -> !t.isDone())
                    .peek(t -> t.setLeftBehind(DateManager.now().toLocalDate()))
                    .forEach(taskService::save);
        }

        return "redirect:/";
    }

    @GetMapping("/pussyMeter")
    public String showPussyMeter(Model model, Principal principal) {
        model.addAttribute("user", getUser(principal));
        return "pussyMeter";
    }

    @PostMapping("/changeMeter")
    public String postPussyMeter(Principal principal, Integer pussyMeter) {
        getUser(principal).setPussyMeter(pussyMeter);
        userService.save(getUser(principal));
        return "redirect:/";
    }

    @GetMapping("planned")
    public String showPlanned(Model model, Principal principal) {
        model.addAttribute("planned",
                taskService.sortTasksByYearAndWeek(taskService.findPlannedTasks(getUser(principal), DateManager.formattedCurrentWeek())));
        model.addAttribute("user", getUser(principal));
        return "planned";
    }

    @GetMapping("leftBehind")
    public String showLeftBehind(Model model, Principal principal) {
        List<Task> leftBehind = taskService.findLeftBehind(getUser(principal));

        model.addAttribute("leftBehind", leftBehind);
        model.addAttribute("user", getUser(principal));
        return "leftBehind";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }
}
