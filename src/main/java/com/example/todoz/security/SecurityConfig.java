package com.example.todoz.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService inMemoryUsers() {
        UserDetails ales = User.builder()
                .username("ales")
                .password(passwordEncoder().encode("ales"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails bery = User.builder()
                .username("bery")
                .password(passwordEncoder().encode("bery"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails dom = User.builder()
                .username("dom")
                .password(passwordEncoder().encode("dom"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails dave = User.builder()
                .username("dave")
                .password(passwordEncoder().encode("dave"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails vadim = User.builder()
                .username("vadim")
                .password(passwordEncoder().encode("vadim"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails honza = User.builder()
                .username("honza")
                .password(passwordEncoder().encode("honza"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder().encode("tom"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(ales, bery, dom, dave, vadim, honza, tom);
    }
}
