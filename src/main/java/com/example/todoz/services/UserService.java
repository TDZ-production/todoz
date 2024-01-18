package com.example.todoz.services;

import com.example.todoz.models.User;
import com.example.todoz.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
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
//                    user.getRoles()
//                    .stream()
//                    .map(r -> new SimpleGrantedAuthority(r.getName()))
//                    .collect(Collectors.toSet());
            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        }
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public User findById(Long userId) {
        return userRepo.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    public void createAndSave(String username, String password, Integer pussyMeter) {
        User user = new User(username, password, pussyMeter);
        save(user);
    }

    public void updatePassword(Long userId, String password) {
        User user = findById(userId);
        user.setPassword(password);
        save(user);
    }
}
