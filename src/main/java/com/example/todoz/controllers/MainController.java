package com.example.todoz.controllers;

import com.example.todoz.models.Task;
import com.example.todoz.models.Week;
import com.example.todoz.services.NotificationService;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.WeekService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {

    private final TaskService taskService;

    private final WeekService weekService;

    private final NotificationService notificationService;

    public MainController(TaskService taskService, WeekService weekService, NotificationService notificationService) {
        this.taskService = taskService;
        this.weekService = weekService;
        this.notificationService = notificationService;
    }

    @GetMapping({"", "/"})
    public String showIndex(Model model) {
        Optional<Week> currentWeek = weekService.findCurrentWeek();

        if (currentWeek.isEmpty()) {
            Week newWeek = new Week();
            weekService.save(newWeek);
            model.addAttribute("currentWeek", newWeek);
        }
        else {
            model.addAttribute("currentWeek", currentWeek.get());
        }

        model.addAttribute("messages", notificationService.getNotificationWithSameDay(taskService.getAllAndSortByPriority().stream()
                .filter(t -> !t.isDone())
                .findFirst().orElse(null)));

        return "index";
    }

    @PostMapping("/add")
    public String add(Task task, LocalDate maybeDueDate) {

        if (maybeDueDate == null) {
            task.setWeek(weekService.findCurrentWeek().get());
        } else if (maybeDueDate.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear()) == Week.getCurrentWeekNumber()) {
            task.setWeek(weekService.findCurrentWeek().get());
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        } else {
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        }

        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("/weekReview")
    public String showWeekReview(Model model) {
        Week currentWeek = weekService.findCurrentWeek().get();
        List<Task> upcomingTasks = taskService.findTasksForNextWeek();

        model.addAttribute("currentWeek", currentWeek);
        model.addAttribute("upcomingTasks", upcomingTasks);
        model.addAttribute("howManyTasks", currentWeek.getNumberOfNotDoneTasks() + upcomingTasks.size());
        return "weekReview";
    }

    @PostMapping("/createNewWeek")
    public String startNewWeek() {
        Week newWeek = new Week();
        newWeek.setWeekNumber(Week.getCurrentWeekNumber() + 1);

        List<Task> tasks = Stream.concat(
                        weekService.findCurrentWeek().get().getNotDoneTasks().stream(),
                        taskService.findTasksForNextWeek().stream())
                .peek(t -> t.setWeek(newWeek))
                .collect(Collectors.toList());

        newWeek.setTasks(tasks);
        weekService.save(newWeek);

        return "redirect:/";
    }

    @GetMapping("longTerm")
    public String showLongTerm(Model model) {
        model.addAttribute("longTerm", taskService.findLongTerm());
        return "longTerm";
    }

    @PostMapping("/checked/{id}")
    public String checkedTask(@PathVariable Long id, @RequestParam boolean done) {
        taskService.checkedTask(id, done);
        return "redirect:/";
    }

}
