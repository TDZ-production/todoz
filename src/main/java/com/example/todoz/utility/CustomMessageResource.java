package com.example.todoz.utility;

import com.example.todoz.user.User;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class CustomMessageResource {

    private MessageSource messageSource;
    public String getMessageSource(User user, String code, Locale locale) {
        ReloadableResourceBundleMessageSource messageSource1 = new ReloadableResourceBundleMessageSource();
        messageSource1.setBasename("classpath:/messages_cs_aggressive");
        return messageSource.getMessage(code, null, locale);
    }
}