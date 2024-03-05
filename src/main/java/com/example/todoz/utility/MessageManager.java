package com.example.todoz.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private static Map<Language, Map<String, String[]>> db = new HashMap<>();

    static {
        ObjectMapper mapper = new ObjectMapper();

        for (Language language : Language.values()) {
            try {
                db.put(language, mapper.readValue(ResourceUtils.getFile(String.format("classpath:messages_%s.json", language)), new TypeReference<Map<String, String[]>>() {}));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getString(String key, Language lang, Integer pussyMeter) {

        String[] labels = db.get(lang).get(key);

        return labels[pussyMeter % labels.length];
    }
}
