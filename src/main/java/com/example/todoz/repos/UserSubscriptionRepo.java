package com.example.todoz.repos;

import com.example.todoz.models.UserSubscription;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public interface UserSubscriptionRepo extends ListCrudRepository<UserSubscription, Long> {

    Optional<UserSubscription> findByAuthKey(String auth);

}
