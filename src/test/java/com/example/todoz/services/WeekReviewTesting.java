package com.example.todoz.services;

import com.example.todoz.controllers.MainController;
import com.example.todoz.models.DateManager;
import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.services.TaskService;
import com.example.todoz.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeekReviewTesting {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DateManager dateManager;

    @Mock
    private WeekService weekService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private MainController mainController;

    @Test
    public void WeekReview_CanAddTask_True() throws Exception {
        // arrange
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("TestPassword");

        Task task = new Task();
        task.setDescription("TestTask");
        task.setDueDate(LocalDateTime.now().plusWeeks(1));

        when(taskService.findUpcomingTasks(any(User.class), anyInt(), anyInt()))
                .thenReturn(List.of(task));
        when(weekService.getCurrentWeek(any(User.class)))
                .thenReturn(Optional.of(null));
        when(weekService.findPreviousWeek(any(User.class)))
                .thenReturn(List.of(task));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("TestUser");
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("TestUser");

        //act
        ResultActions resultActions = mockMvc.perform(get("/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("weekReview"))
                .andExpect(model().attributeExists("user", "previousWeek", "upcomingTasks"));
    }
}
