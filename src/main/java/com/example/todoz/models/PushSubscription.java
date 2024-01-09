package com.example.todoz.models;

import nl.martijndwars.webpush.Subscription;

import java.security.Principal;

public record PushSubscription(Principal principal, Subscription subscription) {
}
