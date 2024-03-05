package com.example.todoz.utility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MessageManager {
    private static Map<Language, Map<String, String[]>> textDataBase = new HashMap<>();

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

    public static String getString(String key, Language lang, Integer pussyMeter) {

        String[] labels = textDataBase.get(lang).get(key);

        return labels[pussyMeter % labels.length];
    }
}
