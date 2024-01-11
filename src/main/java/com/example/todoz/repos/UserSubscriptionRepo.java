package com.example.todoz.repos;

import com.example.todoz.models.UserSubscription;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface UserSubscriptionRepo extends ListCrudRepository<UserSubscription, Long> {


}
