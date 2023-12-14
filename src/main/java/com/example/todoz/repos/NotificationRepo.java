package com.example.todoz.repos;

import com.example.todoz.models.Notification;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends ListCrudRepository<Notification,Long> {
}
