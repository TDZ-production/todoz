package com.example.todoz.controllers;

import com.example.todoz.notification.NotificationService;
import com.example.todoz.utility.*;
import com.example.todoz.task.Task;
import com.example.todoz.task.TaskService;
import com.example.todoz.user.User;
import com.example.todoz.user.UserService;
import com.example.todoz.week.Week;
import com.example.todoz.week.WeekService;
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
    private final NotificationService notificationService;

    @GetMapping
    public String showIndex(Model model, Principal principal) {
        User user = getUser(principal);
        Optional<Week> currentWeek = weekService.findCurrentWeek(user);
        Optional<Week> optPreviousWeek = weekService.findPreviousWeek(user);


        if (currentWeek.isEmpty() && optPreviousWeek.isPresent()) {
            Week previousWeek = optPreviousWeek.get();
            List<Task> upcomingTasks = taskService
                    .findUpcomingTasks(user, previousWeek.getWeekNumber(), DateManager.formattedCurrentWeek());

            model.addAttribute("user", user);
            model.addAttribute("previousWeek", previousWeek);
            model.addAttribute("upcomingTasks", upcomingTasks);
            model.addAttribute("currentWeekNumber", DateManager.getWeekNumber());
            model.addAttribute("graphData", taskService.getGraphData(user));

            return "weekReview";
        } else if (currentWeek.isEmpty()) {
            Week week = new Week(user);
            weekService.save(week);

            model.addAttribute("currentWeek", week);
        } else {
            model.addAttribute("currentWeek", currentWeek.get());
        }

        model.addAttribute("user", user);
        model.addAttribute("publicKey", notificationService.getPublicKey());
        model.addAttribute("message", user.getText("index_body"));

        return "index";
    }

    @PostMapping("/startNewWeek")
    public String startNewWeek(Principal principal, @RequestParam(value = "taskIds", required = false) List<Long> taskIds, @RequestParam(value = "leftBehinds", required = false) List<Long> leftBehinds) {
        User user = getUser(principal);
        Week week = new Week(user);
        weekService.save(week);

        if (taskIds != null && !taskIds.isEmpty()) {
            taskIds.stream()
                    .map(taskId -> taskService.findTaskByIdAndUserId(taskId, user))
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
                    .map(leftID -> taskService.findTaskByIdAndUserId(leftID, user))
                    .filter(t -> !t.isDone())
                    .peek(t -> t.setLeftBehind(DateManager.now()))
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
        User user = getUser(principal);

        user.setPussyMeter(pussyMeter);
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("planned")
    public String showPlanned(Model model, Principal principal) {
        User user = getUser(principal);

        model.addAttribute("planned",
                taskService.mapTasksByYearAndWeek(taskService.findPlannedTasks(user)));
        model.addAttribute("user", user);
        model.addAttribute("message", user.getText("planned"));
        return "planned";
    }

    @GetMapping("leftBehind")
    public String showLeftBehind(Model model, Principal principal) {
        User user = getUser(principal);
        List<Task> leftBehind = taskService.findLeftBehind(user);

        model.addAttribute("leftBehind", leftBehind);
        model.addAttribute("user", user);
        model.addAttribute("message", user.getText("left_behind"));
        return "leftBehind";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName())
                .orElseThrow(EntityNotFoundException::new);
    }
}
