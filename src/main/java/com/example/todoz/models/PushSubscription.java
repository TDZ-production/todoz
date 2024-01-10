package com.example.todoz.models;

import nl.martijndwars.webpush.Subscription;

public record PushSubscription(User user, Subscription subscription) {
}
