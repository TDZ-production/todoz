package com.example.todoz.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUser(Principal principal) {
        return findByUsername(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> temp = userRepo.findByUsername(username);

        if (temp.isEmpty()) {
            throw new UsernameNotFoundException("User with this username not found");
        }
        else {
            User user = temp.get();
            Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("USER"));
            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepo.findById(userId);
    }

    public void createAndSave(String username, String password, Integer pussyMeter) {
        User user = new User(username, password, pussyMeter);
        save(user);
    }

    public void updatePassword(Long userId, String password) {
        User user = findById(userId).orElseThrow(EntityNotFoundException::new);
        user.setPassword(password);
        save(user);
    }
}
