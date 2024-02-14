package com.example.todoz.controllers;

import com.example.todoz.models.*;
import com.example.todoz.services.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final TaskService taskService;
    private final WeekService weekService;
    private final NotificationService notificationService;

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
        model.addAttribute("publicKey", notificationService.getPublicKey());

        return "index";
    }
    // Endpoint for testing the chart - delete before merge
    @GetMapping("/test")
    public String showTest(Model model, Principal principal) {
        model.addAttribute("user", getUser(principal));
        Optional<Week> currentWeek = weekService.findCurrentWeek(getUser(principal));
        Optional<Week> optPreviousWeek = weekService.findPreviousWeek(getUser(principal));
        Week previousWeek = optPreviousWeek.get();
//        System.out.println(taskService.findWeekdayReviews(getUser(principal)));

        List<List<Double>> testList = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i<7; i++) {
            testList.add(new ArrayList<>());
            for (int j=0; j<4; j++) {
                testList.get(i).add(random.nextDouble());
            }
        }

        List<Task> upcomingTasks = taskService
                .findUpcomingTasks(getUser(principal), previousWeek.getWeekNumber(), DateManager.formattedCurrentWeek());
        model.addAttribute("previousWeek", previousWeek);
        model.addAttribute("graphData", taskService.getGraphData(getUser(principal)));
        model.addAttribute("upcomingTasks", upcomingTasks);
        return "weekReview";
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
        getUser(principal).setPussyMeter(pussyMeter);
        userService.save(getUser(principal));
        return "redirect:/";
    }

    @GetMapping("planned")
    public String showPlanned(Model model, Principal principal) {
        model.addAttribute("planned",
                taskService.mapTasksByYearAndWeek(taskService.findPlannedTasks(getUser(principal), DateManager.formattedCurrentWeek())));
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
