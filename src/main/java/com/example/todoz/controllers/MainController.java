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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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

    @PostMapping("/startNewWeek")
    public String startNewWeek(Principal principal, @RequestParam("tasks") List<Long> delegatedTasks) {
        Week week = new Week(getUser(principal));
        delegatedTasks.stream()
                        .map(taskId -> taskService.findTaskByIdAndUserId(taskId, getUser(principal)))
                        .forEach(task -> task.setWeek(week));
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
        model.addAttribute("longTerm", taskService.findLongTermTasks(getUser(principal)));
        return "longTerm";
    }

    @PostMapping("/checked/{id}")
    public String checkedTask(@PathVariable Long id, @RequestParam boolean done) {
        // TODO: Fix me, get me some Principal!
        taskService.checkedTask(id, done);
        return "redirect:/";
    }

    @GetMapping("/test")
    public String showTest() {
        return "test";
    }

    @PostMapping("/test")
    public String receiveArray(@RequestParam("numbers") List<Integer> numbers, Model model) {
        if (numbers.size() > 1){
            model.addAttribute("numbers", numbers);
            return "testPositive";
        }
        else {
            return "testNegative";
        }
    }

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.getCurrentWeek(getUser(principal));
    }
}
