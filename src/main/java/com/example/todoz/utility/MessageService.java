package com.example.todoz.utility;

import com.example.todoz.task.Task;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import com.example.todoz.week.WeekService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final WeekService weekService;
    private final static Map<Language, Map<String, String[]>> textDataBase = new HashMap<>();

    static {
//        for (Language language : Language.values()) {
//            try (Scanner scanner = new Scanner(new File(String.format("src/main/resources/languagePackages/messages_%s.csv", language)))) {
//                Map<String, String[]> languageText = new HashMap<>();
//                while(scanner.hasNextLine()){
//                    String[] parts = scanner.nextLine().split(";");
//                    languageText.put(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
//                }
//                textDataBase.put(language, languageText);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/languagePackages/messages_EN.csv"))) {
            String[] lineInArray;
            Map<String, String[]> testText = new HashMap<>();
            while ((lineInArray = reader.readNext()) != null) {
                testText.put(lineInArray[0], Arrays.copyOfRange(lineInArray, 1, lineInArray.length));
            }
            textDataBase.put(Language.EN, testText);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInAppText(String key, Language lang, Integer pussyMeter) {

        String[] labels = textDataBase.get(lang).get(key);

        if (labels == null) {
            return String.format("##%s##", key);
        }

        return labels[pussyMeter % labels.length];
    }

    public static String getInAppText(String key, Language lang, Integer pussyMeter, String param) {

        String[] labels = textDataBase.get(lang).get(key);

        return String.format(labels[pussyMeter % labels.length], param);
    }

    public String getSpamNotification(User user) {

        Optional<Week> optWeek = weekService.findCurrentWeek(user);

        if (optWeek.isPresent()) {
            Week week = optWeek.get();

            List<Task> tasksToday = week.getTasksForNotification()
                    .stream()
                    .filter(t -> t.getPriority() > 3 && t.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(10)))
                    .sorted(Comparator.comparing(t -> new Random().nextInt()))
                    .toList()

            if (!tasksToday.isEmpty()) {
                String[] bodyMessages = {
                        "What are you doing with your life?!",
                        "Just do it!",
                        "Get your act together!",
                        "Is it done yet?",
                        "I love you"
                };
                String body = bodyMessages[new Random().nextInt(bodyMessages.length)];

                String tasks = tasksToday.stream()
                        .map(t -> "🔥 " + t.getDescription().replaceAll("\"", "\'"))
                        .collect(Collectors.joining("\\n"));

                String title = String.format("%d ", tasksToday.size()) +
                    (tasksToday.size() == 1 ? "task is" : "tasks are") +
                    " on fire";

                return String.format("{ \"title\": \"%s\", \"body\": \"%s\\n%s\" }", title, tasks, body);
            }
        }
        return null;
    }

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
