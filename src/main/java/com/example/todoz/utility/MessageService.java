package com.example.todoz.utility;

import com.example.todoz.task.Task;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import com.example.todoz.week.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final WeekService weekService;
    private final static Map<Language, Map<String, String[]>> textDataBase = new HashMap<>();

    static {
        for (Language language : Language.values()) {
            try (Scanner scanner = new Scanner(new File(String.format("src/main/resources/messages_%s.csv", language)))) {
                Map<String, String[]> languageText = new HashMap<>();
                while(scanner.hasNextLine()){
                    String[] parts = scanner.nextLine().split(":");
                    languageText.put(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
                }
                textDataBase.put(language, languageText);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getInAppText(String key, Language lang, Integer pussyMeter) {

        String[] labels = textDataBase.get(lang).get(key);

        return labels[pussyMeter % labels.length];
    }

    public String getNotification(User user) {

        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            Week week = optWeek.get();

            List<Task> tasks = week.getTasksForNotification();

            if (!tasks.isEmpty()) {

                List<Task> tasksToday = tasks.stream()
                        .filter(t -> t.getDueDate() != null)
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
