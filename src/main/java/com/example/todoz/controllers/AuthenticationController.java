package com.example.todoz.controllers;

import com.example.todoz.dtos.RegisterDTO;
import com.example.todoz.prtoken.PasswordResetToken;
import com.example.todoz.services.EmailService;
import com.example.todoz.models.User;
import com.example.todoz.prtoken.PRTService;
import com.example.todoz.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.todoz.models.User.MINIMAL_PASSWORD_LENGTH;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PRTService pRTService;
    private final EmailService emailService;

    @GetMapping("/login")
    public String showLogin() {
        return "loginPage";
    }

    @PostMapping("/")
    public String login() {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("minimalPasswordLength", MINIMAL_PASSWORD_LENGTH);
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(RegisterDTO registerDTO, RedirectAttributes ra) {
        Optional<User> optUSer = userService.findByUsername(registerDTO.username());

        if (optUSer.isPresent()) {
            ra.addFlashAttribute("userExists", true);

            return "redirect:/register";
        } else if (!isValidUsername(registerDTO.username()) || !isValidPassword(registerDTO.password())) {
            ra.addFlashAttribute("invalidCredentials", true);

            return "redirect:/register";
        } else {
            userService.createAndSave(registerDTO.username(), passwordEncoder.encode(registerDTO.password()), registerDTO.pussyMeter());

            return "redirect:/login";
        }
    }

    @GetMapping("/resetPassword")
    public String showForgotPassword() {
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordRequest(String email, final HttpServletRequest request, RedirectAttributes ra) {
        Optional<User> optUser = userService.findByUsername(email);

        if (optUser.isEmpty()) {
            ra.addFlashAttribute("userNotFound", true);

            return "redirect:/resetPassword";
        }
        User user = optUser.get();
        emailService.sendEmail(user.getUsername(), passwordResetEmailLink(request, pRTService.createPRToken(user).getToken()));
        ra.addFlashAttribute("sent", true);

        return "redirect:/resetPassword";
    }

    @GetMapping("/validateToken")
    public String showNewPassword(@RequestParam(required = false) String token, RedirectAttributes ra) {
        Optional<PasswordResetToken> optToken = pRTService.findByToken(token);

        if (token == null || token.isEmpty() || optToken.isEmpty()) {
            ra.addFlashAttribute("tokenNotFound", true);
            return "redirect:/resetPassword";
        } else if (!pRTService.validateToken(optToken.get())) {
            ra.addFlashAttribute("tokenExpired", true);
            pRTService.deleteByToken(token);
            return "redirect:/resetPassword";
        } else {
            ra.addFlashAttribute("userId", optToken.get().getUser().getId());
            return "redirect:/newPassword";
        }
    }

    @GetMapping("/newPassword")
    public String showNewPassword() {
        return "newPassword";
    }

    @PostMapping("/newPassword")
    public String resetPassword(String password, String confirmation, RedirectAttributes ra, Long userId) {
        if (!password.equals(confirmation)) {
            ra.addFlashAttribute("doesNotMatch", true);
            ra.addFlashAttribute("userId", userId);
            return "redirect:/newPassword";
        } else if (password.length() < MINIMAL_PASSWORD_LENGTH) {
            ra.addFlashAttribute("invalid", true);
            ra.addFlashAttribute("userId", userId);
            return "redirect:/newPassword";
        }

        pRTService.deleteAllByUserId(userId);
        userService.updatePassword(userId, passwordEncoder.encode(password));
        ra.addFlashAttribute("passwordReset", true);

        return "redirect:/login";
    }

    private String passwordResetEmailLink(HttpServletRequest request, String token) {
        return "https://" + request.getServerName() + request.getContextPath() + "/validateToken?token=" + token;
    }

    public boolean isValidPassword(String password) {
        return password.length() >= MINIMAL_PASSWORD_LENGTH;
    }

    public boolean isValidUsername(String username) {
        String emailRegEx = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(emailRegEx)
                .matcher(username)
                .matches();
    }
}