package com.example.todoz.prtoken;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PRTRepo extends ListCrudRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByUserId(Long id);
}
