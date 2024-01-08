package com.example.todoz.controllers;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if(currentWeek.isEmpty() && optPreviousWeek.isPresent()) {
            Week previousWeek = optPreviousWeek.get();
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

        //part of the messages

        model.addAttribute("messages", null);

        return "index";
    }

    @PostMapping("/add")
    public String add(Task task, LocalDate maybeDueDate, Principal principal) {

        task.digestDueDate(maybeDueDate, getWeek(principal));
        task.setUser(getUser(principal));

        taskService.save(task);

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}")
    public String update(@PathVariable Long id, TaskUpdateDTO taskUpdate, Principal principal) {
        taskService.update(id, taskUpdate, getUser(principal), getWeek(principal));

        return "redirect:/";
    }

    @PostMapping("/startNewWeek")
    public String startNewWeek(Principal principal) {
        Week week = new Week(getUser(principal));
        Week previousWeek = weekService.getPreviousWeek(getUser(principal));

        List<Task> tasks = Stream.concat(
                        previousWeek.getNotDoneTasks().stream(),
                        taskService.findTasksForThisWeek(getUser(principal)).stream())
                .peek(t -> t.setWeek(week))
                .collect(Collectors.toList());

        week.setTasks(tasks);
        weekService.save(week);

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

    private User getUser(Principal principal) {
        return userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new);
    }

    private Week getWeek(Principal principal) {
        return weekService.getCurrentWeek(getUser(principal));
    }
}
