package com.example.todoz.controllers;

import com.example.todoz.services.TaskService;
import com.example.todoz.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("check/{id}")
    public ResponseEntity<Void> checkTask(@PathVariable Long id, @RequestParam boolean done, Principal principal) {
        taskService.checkedTask(id, userService.findByUsername(principal.getName()).orElseThrow(RuntimeException::new), done);
        return ResponseEntity.ok().build();
    }
}
