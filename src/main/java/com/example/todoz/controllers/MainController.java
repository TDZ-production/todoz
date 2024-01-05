package com.example.todoz.controllers;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import com.example.todoz.services.NotificationService;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.UserService;
import com.example.todoz.services.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final TaskService taskService;
    private final WeekService weekService;
    private final NotificationService notificationService;

    @GetMapping
    public String showIndex(Model model, Principal principal) {
        Week currentWeek = getWeek(principal);

        model.addAttribute("currentWeek", currentWeek);

        model.addAttribute("messages", notificationService.getNotificationWithSameDay(taskService.getAllAndSortByPriority().stream()
                .filter(t -> !t.isDone())
                .findFirst().orElse(null)));

        return "index";
    }

    @PostMapping("/add")
    public String add(Task task, LocalDate maybeDueDate, Principal principal) {

        if (maybeDueDate == null) {
            task.setWeek(getWeek(principal));
        } else if (maybeDueDate.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear()) == Week.getCurrentWeekNumber()) {
            task.setWeek(getWeek(principal));
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        } else {
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        }

        task.setUser(getUser(principal));

        taskService.save(task);

        return "redirect:/";
    }

    @PutMapping("/tasks")
    public String update(TaskUpdateDTO taskUpdate, Principal principal) {
        taskService.update(taskUpdate, getUser(principal));

        return "redirect:/";
    }

    @GetMapping("/weekReview")
    public String showWeekReview(Model model, Principal principal) {
        Week currentWeek = getWeek(principal);
        List<Task> upcomingTasks = taskService.findTasksForNextWeek(getUser(principal));

        model.addAttribute("currentWeek", currentWeek);
        model.addAttribute("upcomingTasks", upcomingTasks);
        model.addAttribute("howManyTasks", currentWeek.getNumberOfNotDoneTasks() + upcomingTasks.size());
        return "weekReview";
    }


    @PostMapping("/createNewWeek")
    public String startNewWeek(Principal principal) {
        Week newWeek = new Week();
        newWeek.setWeekNumber(Week.getCurrentWeekNumber() + 1);

        List<Task> tasks = Stream.concat(
                        getWeek(principal).getNotDoneTasks().stream(),
                        taskService.findTasksForNextWeek(getUser(principal)).stream())
                .peek(t -> t.setWeek(newWeek))
                .collect(Collectors.toList());

        newWeek.setTasks(tasks);
        weekService.save(newWeek);

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

    @GetMapping("longTerm")
    public String showLongTerm(Model model, Principal principal) {
        model.addAttribute("longTerm", taskService.findLongTerm(getUser(principal)));
        return "longTerm";
    }

    @PostMapping("/checked/{id}")
    public String checkedTask(@PathVariable Long id, @RequestParam boolean done) {
        // TODO: Fix me, get me some Principal!!!
        taskService.checkedTask(id, done);
        return "redirect:/";
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.getCurrentWeek(getUser(principal));
    }
}
