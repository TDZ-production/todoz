package com.example.todoz.repos;

import com.example.todoz.models.Role;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleRepo extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
