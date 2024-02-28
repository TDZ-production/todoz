package com.example.todoz.userSub;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionService {

    private final UserSubscriptionRepo userSubscriptionRepo;


    public UserSubscriptionService(UserSubscriptionRepo userSubscriptionRepo) {
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public Optional<UserSubscription> findByAuth( String authKey){
        return userSubscriptionRepo.findByAuthKey(authKey);
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
