package com.example.todoz.notification;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


import com.example.todoz.user.User;
import com.example.todoz.userSub.UserSubscription;
import com.example.todoz.userSub.UserSubscriptionService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

@Service
@Getter
public class NotificationService {

    @Value("${vapid.public.key}")
    private String publicKey;
    @Value("${vapid.private.key}")
    private String privateKey;

    private PushService pushService;
    private final UserSubscriptionService userSubscriptionService;

    public NotificationService(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }


    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey);
    }

    public String subscribe(Subscription subscription, User user) {
        Optional<UserSubscription> maybeUserSub = userSubscriptionService.findByAuth(subscription.keys.auth);

        if (maybeUserSub.isEmpty()) {
            UserSubscription userSubscription = new UserSubscription(subscription, user);
            userSubscriptionService.save(userSubscription);
            return "Subscription saved successfully.";
        } else {
            return "This subscription already exists.";
        }
    }

    public void sendNotification(UserSubscription userSubscription, String messageJson) {
        try {
            System.out.println("\nsending notification!!");
            Subscription subscription = new Subscription(userSubscription.getEndpoint(), new Subscription.Keys(userSubscription.getP256dhKey(), userSubscription.getAuthKey()));
            var resp = pushService.send(new Notification(subscription, messageJson));
            System.out.println(resp.getStatusLine().getStatusCode());
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
                 | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNotification(User user) {
        String out =  String.format("{ \"title\": \"%s\", \"body\": \"%s\" }", "TEST", "MESSAGE");

        userSubscriptionService.getAll().forEach(userSub -> {
            if (userSub.getUser().getUsername().equals(user.getUsername())) {
                sendNotification(userSub, out);
            }
        });
    }
}