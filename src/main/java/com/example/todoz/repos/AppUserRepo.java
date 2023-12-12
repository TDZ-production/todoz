package com.example.todoz.repos;

import com.example.todoz.models.AppUser;
import org.springframework.data.repository.ListCrudRepository;

public interface AppUserRepo extends ListCrudRepository<AppUser, Long> {
}
