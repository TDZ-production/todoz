package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final WeekService weekService;

    public String getNotification(User user) {

        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            Week week = optWeek.get();

            List<Task> tasks = week.getTasksForNotification();

            if (!tasks.isEmpty()) {

                List<Task> tasksToday = tasks.stream()
                        .filter(t -> t != null && t.getDueDate() != null && t.getDueDate().toLocalDate().equals(LocalDate.now()))
                        .toList();

                String title = "Wassup mf!";
                String body = getString(tasksToday, tasks);
                return String.format("{ \"title\": \"%s\", \"body\": \"%s\" }", title, body);
            }
        }
        return null;
    }

    private static String getString(List<Task> tasksToday, List<Task> tasks) {
        String body = "First task is: \\n";
        if (!tasksToday.isEmpty()) {
            body += tasks.get(0).getDescription() + "\\n" + tasksToday.size() + (tasksToday.size() == 1 ? " task is " : " tasks are ") + "due today";
        } else {
            body += tasks.get(0).getDescription();
        }
        return body;
    }
}
