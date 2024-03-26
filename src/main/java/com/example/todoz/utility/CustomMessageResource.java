package com.example.todoz.utility;

import com.example.todoz.user.User;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomMessageResource extends ReloadableResourceBundleMessageSource {
    public String getMessageResource(User user, String code, Locale locale) {
        String basename = user.getPussyMeter() == 2 ? "messages_" + locale.getLanguage() + "_aggressive" : "messages_" + locale.getLanguage() + "_neutral";
        return getMessage(code, null, basename, locale);
    }
}