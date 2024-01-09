package com.example.todoz.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.Security;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.example.todoz.models.PushSubscription;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

@Service
@Getter
public class MessageService {

    @Value("${vapid.public.key}")
    private String publicKey;
    @Value("${vapid.private.key}")
    private String privateKey;

    private PushService pushService;
    private final Map<String, PushSubscription> subscriptions = new HashMap<>();

    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey);
    }

    public void subscribe(Subscription subscription, Principal principal) {
        System.out.println("Subscribed to " + subscription.endpoint);
        this.subscriptions.put(subscription.keys.auth, new PushSubscription(principal, subscription));
    }


    public void unsubscribe(Subscription subscription) {
        System.out.println("Unsubscribed from " + subscription.endpoint);
        subscriptions.forEach((key, sub) -> {
            if(key.equals(subscription.keys.auth)) {
                subscriptions.remove(key);
            }
        });
    }


    public void sendNotification(Subscription subscription, String messageJson) {
        try {
            pushService.send(new Notification(subscription, messageJson));
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
                 | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendNotifications(Principal principal, Subscription subscription) {


        var json = """
        {
          "title": "Server says hello to %s !",
          "body": "hello"
        }
        """.formatted(principal.getName());

        List<Map.Entry<String, PushSubscription>> listUserSubscription = subscriptions.entrySet().stream()
                .filter(sub -> sub.getValue().principal().getName().equals(principal.getName()))
                .filter(sub -> sub.getKey().equals(subscription.keys.auth))
                .toList();

        if(listUserSubscription.size() > 1){
            unsubscribe(subscription);
        }else{
                sendNotification(listUserSubscription.get(0).getValue().subscription(), String.format(json, LocalTime.now()));

        }


    }
}