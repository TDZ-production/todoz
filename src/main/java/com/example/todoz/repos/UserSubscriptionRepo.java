package com.example.todoz.repos;

import com.example.todoz.models.UserSubscription;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionRepo extends ListCrudRepository<UserSubscription, Long> {

    Optional<UserSubscription> findByAuthKey(String auth);

}
