package com.example.todoz.prtoken;

import com.example.todoz.models.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PRTService {

    private final PRTRepo repo;

    public void save(PasswordResetToken token) {
        repo.save(token);
    }

    public PasswordResetToken createPRToken(User user) {
        PasswordResetToken token = new PasswordResetToken(user);
        save(token);

        return token;
    }

    public boolean validateToken(PasswordResetToken token) {
        return LocalDateTime.now().isBefore(token.getExpirationTime());
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    @Transactional
    public void deleteByToken(String token) {
        repo.deleteByToken(token);
    }

    @Transactional
    public void deleteAllByUserId(Long id) {
        repo.deleteAllByUserId(id);
    }
}
