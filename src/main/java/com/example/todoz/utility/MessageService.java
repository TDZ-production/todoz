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
@Transactional
public class MessageService {

    public MessageService(WeekService weekService, CustomMessageResource messageResource) {
        this.weekService = weekService;
        this.messageResource = messageResource;
    }

    private final WeekService weekService;
    private final CustomMessageResource messageResource;

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

    public String getMessageForUser(User user, String code, Locale locale) {
        return messageResource.getMessageResource(user, code, locale);
    }
}
