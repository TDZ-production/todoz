package com.example.todoz.repos;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends ListCrudRepository<Task, Long> {
    List<Task> findAllByPriority(Integer priority);
    List<Task> findByUserId(Long id);

    Optional<Task> findByIdAndUser(Long id, User u);
}
