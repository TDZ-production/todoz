package com.example.todoz.repos;

import com.example.todoz.models.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends ListCrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
