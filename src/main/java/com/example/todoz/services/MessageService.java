package com.example.todoz.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;


import com.example.todoz.models.User;
import com.example.todoz.models.UserSubscription;
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
    private final UserSubscriptionService userSubscriptionService;

    public MessageService(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }


    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey);
    }

    public void subscribe(Subscription subscription, User user) {
        System.out.println("Subscribed to " + subscription.endpoint);
        UserSubscription userSubscription =  new UserSubscription(subscription, user);
        userSubscriptionService.save(userSubscription);

        List<UserSubscription> userSubscriptionList = userSubscriptionService.getAll().stream()
                .filter(userSub -> userSub.getAuthKey().equals(subscription.keys.auth))
                .toList();

        if(userSubscriptionList.size() > 1){
            unsubscribe(userSubscription);
        }
    }


    public void unsubscribe(UserSubscription userSubscription) {
        System.out.println("Unsubscribed from " + userSubscription.getEndpoint());
        userSubscriptionService.remove(userSubscription);
    }


    public void sendNotification(UserSubscription userSubscription, String messageJson) {
        try {
            Subscription subscription = new Subscription(userSubscription.getEndpoint(), new Subscription.Keys(userSubscription.getP256dhKey(), userSubscription.getAuthKey()));
            pushService.send(new Notification(subscription, messageJson));
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
                 | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 38 12 * * *")
    public void sendNotifications() {
        System.out.println("The message was sent" + LocalTime.now());

        var json = """
        {
          "title": "Server says hello to %s !",
          "body": "hello"
        }
        """;

        userSubscriptionService.getAll().forEach(userSub -> sendNotification(userSub, String.format(json, userSub.getUser().getUsername())));
    }
}