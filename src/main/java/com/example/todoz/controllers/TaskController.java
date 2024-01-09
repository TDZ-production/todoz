package com.example.todoz.controllers;

import com.example.todoz.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("check/{id}")
    public ResponseEntity<Void> checkTask(@PathVariable Long id, @RequestParam boolean done) {
        // TODO: Fix me, get me some Principal!
        taskService.checkedTask(id, done);
        return ResponseEntity.ok().build();
    }
}
