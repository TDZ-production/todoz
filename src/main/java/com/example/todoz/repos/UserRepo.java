package com.example.todoz.repos;

import com.example.todoz.models.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepo extends ListCrudRepository<User, Long> {
}
