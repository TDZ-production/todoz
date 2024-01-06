package com.example.todoz.controllers;

import com.example.todoz.models.DateManager;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        Optional<Week> currentWeek = weekService.findCurrentWeek(getUser(principal));
        Optional<Week> OptPreviousWeek = weekService.findPreviousWeek(getUser(principal));

        if(currentWeek.isEmpty() && OptPreviousWeek.isPresent()) {
            Week previousWeek = OptPreviousWeek.get();
            List<Task> upcomingTasks = taskService.findTasksForThisWeek(getUser(principal));

            model.addAttribute("previousWeek", previousWeek);
            model.addAttribute("upcomingTasks", upcomingTasks);
            model.addAttribute("howManyTasks", previousWeek.getNumberOfNotDoneTasks() + upcomingTasks.size());
            return "weekReview";
        }
        else if(currentWeek.isEmpty()) {
            Week week = new Week(getUser(principal));
            weekService.save(week);

            model.addAttribute("currentWeek", week);
        }
        else {
            model.addAttribute("currentWeek", currentWeek.get());
        }

        model.addAttribute("messages", notificationService.getNotificationWithSameDay(taskService.getAllAndSortByPriority().stream()
                .filter(t -> !t.isDone())
                .findFirst().orElse(null)));

        return "index";
    }

    @PostMapping("startNewWeek")
    public String startNewWeek(Principal principal) {
        Week week = new Week(getUser(principal));
        Week previousWeek = weekService.findPreviousWeek(getUser(principal)).get();

        List<Task> tasks = Stream.concat(
                        previousWeek.getNotDoneTasks().stream(),
                        taskService.findTasksForThisWeek(getUser(principal)).stream())
                .peek(t -> t.setWeek(week))
                .collect(Collectors.toList());

        week.setTasks(tasks);
        weekService.save(week);

        return "redirect:/";
    }

    @PostMapping("/add")
    public String add(Task task, LocalDate maybeDueDate, Principal principal) {

        if (maybeDueDate == null) {
            task.setWeek(getWeek(principal));
        } else if (DateManager.formatWeek(maybeDueDate).equals(DateManager.formattedCurrentWeek())) {
            task.setWeek(getWeek(principal));
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        } else {
            task.setDueDate(maybeDueDate.atTime(23, 59, 59));
        }

        task.setUser(getUser(principal));

        taskService.save(task);

        return "redirect:/";
    }

    @GetMapping("longTerm")
    public String showLongTerm(Model model, Principal principal) {
        model.addAttribute("longTerm", taskService.findLongTermTasks(getUser(principal)));
        return "longTerm";
    }

    @PostMapping("/checked/{id}")
    public String checkedTask(@PathVariable Long id, @RequestParam boolean done) {
        // TODO: Fix me, get me some Principal!
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
