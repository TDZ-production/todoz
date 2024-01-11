package com.example.todoz.services;

import com.example.todoz.models.UserSubscription;
import com.example.todoz.repos.UserSubscriptionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSubscriptionService {

    private final UserSubscriptionRepo userSubscriptionRepo;


    public UserSubscriptionService(UserSubscriptionRepo userSubscriptionRepo) {
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public List<UserSubscription> getAll(){
        return userSubscriptionRepo.findAll();
    }
    public void save(UserSubscription userSubscription){
        userSubscriptionRepo.save(userSubscription);
    }

    public void remove(UserSubscription userSubscription){
        userSubscriptionRepo.delete(userSubscription);
    }

}
