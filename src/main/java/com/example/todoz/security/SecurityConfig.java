package com.example.todoz.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.requestMatchers("/login", "/register", "/*.css", "/img/*", "/webfonts/*")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form.loginPage("/login").permitAll());

        return http.build();
    }

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
                .password(passwordEncoder().encode("TheSecurityGuy"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(ales, bery, dom, dave, vadim, honza, tom);
    }
}
