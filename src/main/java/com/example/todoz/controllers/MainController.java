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

        // TODO: resolve this
        model.addAttribute("messages", null);

        return "index";
    }

    @PostMapping("/startNewWeek")
    public String startNewWeek(Principal principal, @RequestParam(value = "taskId", required = false) List<Long> taskIds) {
        Week week = new Week(getUser(principal));

        if (taskIds != null && !taskIds.isEmpty()){
            taskIds.stream()
                    .map(taskId -> taskService.findTaskByIdAndUserId(taskId, getUser(principal)))
                    .forEach(task -> task.setWeek(week));
        }

        weekService.save(week);
        return "redirect:/";
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
    public String checkedTask(@PathVariable Long id, @RequestParam boolean done, Principal principal) {
        taskService.checkedTask(id, done, getUser(principal));
        return "redirect:/";
    }

    @GetMapping("/test")
    public String showTest() {
        return "test";
    }

    @PostMapping("/test")
    public String receiveArray(@RequestParam(value = "numbers", required = false) List<Integer> numbers, Model model) {
        if (numbers != null && !numbers.isEmpty()){
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
