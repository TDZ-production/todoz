package com.example.todoz.controllers;

import com.example.todoz.dtos.LoginDTO;
import com.example.todoz.dtos.RegisterDTO;
import com.example.todoz.models.User;
import com.example.todoz.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "loginPage";
    }

    @PostMapping("/login")
    public String authenticate(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(password, username));

        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(RegisterDTO registerDTO, RedirectAttributes ra) {
        Optional<User> temp = userService.findByUsername(registerDTO.username());

        if(temp.isPresent()) {
            ra.addAttribute("userExists", true);
            return "redirect:/register";
        }
        else {
            User user = new User();
            user.setUsername(registerDTO.username());
            user.setPassword(passwordEncoder.encode(registerDTO.password()));
            userService.save(user);
        }

        return "redirect:/login";
    }
}
