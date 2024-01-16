package com.example.todoz.repos;

import com.example.todoz.models.Notification;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends ListCrudRepository<Notification,Long> {
    List<Notification> findAllByTimeSlotAndPussyMeterAndMorningNotificationSingleTask(String timeSlot, int pussyMeter, boolean notificationSingleTask);
}
