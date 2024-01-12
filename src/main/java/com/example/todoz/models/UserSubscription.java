package com.example.todoz.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nl.martijndwars.webpush.Subscription;

import java.time.LocalTime;


@Getter
@Setter
@Entity
public class UserSubscription {


    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String endpoint;
    private String authKey;
    private String p256dhKey;

    private LocalTime created;
    @ManyToOne
    private User user;

    public UserSubscription(Subscription subscription, User user ) {
        this.endpoint = subscription.endpoint;
        this.authKey= subscription.keys.auth;
        this.p256dhKey = subscription.keys.p256dh;
        this.user = user;
        this.created = LocalTime.now();
    }

    public UserSubscription() {

    }
}
