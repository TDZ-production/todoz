package com.example.todoz.controllers;

import com.example.todoz.dtos.RegisterDTO;
import com.example.todoz.models.User;
import com.example.todoz.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLogin() {
        return "loginPage";
    }

    @PostMapping("/")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "registerPage";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(RegisterDTO registerDTO, RedirectAttributes ra) {
        Optional<User> temp = userService.findByUsername(registerDTO.username());

        if(temp.isPresent()) {
            ra.addFlashAttribute("userExists", true);
            return "redirect:/register";
        }

        if (validateUsername(registerDTO.username()) && validatePassword(registerDTO.password())){
            User user = new User();
            user.setUsername(registerDTO.username());
            user.setPassword(passwordEncoder.encode(registerDTO.password()));
            user.setPussyMeter(registerDTO.pussyMeter());
            userService.save(user);
        } else {
            ra.addFlashAttribute("usernameWrong", true);
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    public boolean validateUsername(String username) {
        return !username.isBlank() && username.length() >= 3;
    }

    public boolean validatePassword(String password) {
        return !password.isBlank() && password.length() >= 5;
    }
}
