package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.models.User;
import com.example.todoz.models.Week;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WeekReviewTesting {


    private final MockMvc mockMvc;
    @MockBean
    private WeekService weekService;
    @MockBean
    private TaskService taskService;
    @Autowired
    public WeekReviewTesting(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void WeekReview_CanAddTask_True() throws Exception {
        // arrange
        Task task = new Task();
        task.setDescription("TestTask");
        task.setDueDate(LocalDateTime.now().plusWeeks(1));

        when(taskService.findUpcomingTasks(any(User.class), anyInt(), anyInt()))
                .thenReturn(List.of(task));
        when(weekService.findPreviousWeek(any(User.class)))
                .thenReturn(Optional.of(new Week()));
        when(weekService.findCurrentWeek(any(User.class)))
                .thenReturn(Optional.empty());

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("TestUser");

        //act
        ResultActions resultActions = mockMvc.perform(get("/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("weekReview"))
                .andExpect(model().attributeExists("user", "previousWeek", "upcomingTasks"));

    }
}
