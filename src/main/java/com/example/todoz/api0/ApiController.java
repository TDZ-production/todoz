package com.example.todoz.api0;

import com.example.todoz.user.User;
import com.example.todoz.user.UserService;
import com.example.todoz.week.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/0/")
public class ApiController {

    private final UserService userService;
    private final WeekService weekService;

    @GetMapping
    public ResponseEntity<WeekDTO> list(Principal principal) {
        User u = userService.getUser(principal);
        var optWeek = weekService.findCurrentWeek(u);

        if (optWeek.isEmpty()) {
            return ResponseEntity.status(404).body(new WeekDTO());
        }

        return ResponseEntity.ok(new WeekDTO(optWeek.get()));
    }

}
