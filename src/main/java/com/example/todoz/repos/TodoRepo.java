package com.example.todoz.repos;

import com.example.todoz.models.Todo;
import com.example.todoz.models.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepo extends ListCrudRepository<Todo, AppUser> {
}
