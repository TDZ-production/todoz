package com.example.todoz.repos;

import com.example.todoz.models.Todo;
import com.example.todoz.models.User;
import org.springframework.data.repository.ListCrudRepository;

public interface TodoRepo extends ListCrudRepository<Todo, User> {
}
