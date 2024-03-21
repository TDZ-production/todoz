package com.example.todoz.utility;

import com.example.todoz.task.Task;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import com.example.todoz.week.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            List<Task> tasksToday = week.getTasksForNotification()
                    .stream()
                    .filter(t -> t.getDueDate() != null)
                    .toList();

            if (!tasksToday.isEmpty()) {
                String title = tasksToday.size() + " tasks are due today";
                String body = tasksToday.get(0).getDescription() + "\\nis your first task on the list";
                return String.format("{ \"title\": \"%s\", \"body\": \"%s\" }", title, body);
            }
        }
        return null;
    }
}
