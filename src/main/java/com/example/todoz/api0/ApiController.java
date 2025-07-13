package com.example.todoz.api0;

import com.example.todoz.dtos.TaskUpdateDTO;
import com.example.todoz.task.Task;
import com.example.todoz.task.TaskService;
import com.example.todoz.user.User;
import com.example.todoz.user.UserService;
import com.example.todoz.week.Week;
import com.example.todoz.week.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/0/")
public class ApiController {

    private final UserService userService;
    private final WeekService weekService;
    private final TaskService taskService;

    @Value("${app.frontend.url}")
    private String frontendUrl;
    private String frontendHtml = "refresh to load!";

    @GetMapping("app.html")
    public String appHtml() {
        return frontendHtml;
    }


    @GetMapping
    public ResponseEntity<WeekDTO> list(Principal principal) {
        User u = userService.getUser(principal);
        var optWeek = weekService.findCurrentWeek(u);

        if (optWeek.isEmpty()) {
            return ResponseEntity.status(404).body(new WeekDTO());
        }

        return ResponseEntity.ok(new WeekDTO(optWeek.get()));
    }

    @PostMapping
    public ResponseEntity<Long> add(@RequestBody Task task, LocalDate maybeDueDate, Principal principal) {
        User user = userService.getUser(principal);
        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            task.digestDueDate(maybeDueDate, optWeek.get());
            task.setUser(user);
            taskService.save(task);

            return ResponseEntity.ok(task.getId());
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    @PatchMapping("{id}/done")
    public ResponseEntity<Void> checkTask(@PathVariable Long id, @RequestBody TaskCheckDTO data, Principal principal) {
        taskService.checkedTask(id, userService.getUser(principal), data.done());

        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdate, Principal principal) {
        User user = userService.getUser(principal);
        var optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        taskService.update(id, taskUpdate, user, optWeek.get());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        taskService.leaveBehind(id, userService.getUser(principal));

        return ResponseEntity.ok().build();
    }

    @GetMapping("refresh")
    public ResponseEntity<String> fetchFrontendHtml() {
        fetchCurrentFrontendHtml();
        return ResponseEntity.ok(frontendHtml);
    }

    public void fetchCurrentFrontendHtml() {
        try {
            frontendHtml = new String(java.net.http.HttpClient.newHttpClient()
                    .send(java.net.http.HttpRequest.newBuilder()
                            .uri(java.net.URI.create(frontendUrl))
                            .build(), java.net.http.HttpResponse.BodyHandlers.ofString())
                    .body());
        } catch (Exception e) {

        }
    }
}
