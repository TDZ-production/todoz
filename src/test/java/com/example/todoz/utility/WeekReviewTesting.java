package com.example.todoz.utility;

import com.example.todoz.task.Task;
import com.example.todoz.task.TaskService;
import com.example.todoz.user.User;
import com.example.todoz.week.Week;
import com.example.todoz.user.UserService;
import com.example.todoz.week.WeekService;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WeekReviewTesting {


    private final MockMvc mockMvc;
    @MockBean
    private WeekService weekService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private UserService userService;
    @Autowired
    public WeekReviewTesting(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void WeekReview_ViewHasAddedTasks_True() throws Exception {
        // arrange
        Optional<User> testUser = Optional.of(new User("TestUser", "password", 1));

        Task plannedTask = new Task();
        plannedTask.setDescription("plannedTask");
        plannedTask.setDueDate(LocalDateTime.now().plusWeeks(1));

        Task doneTask = new Task();
        doneTask.setDescription("doneTask");
        doneTask.setDoneAt(DateManager.now());

        Task notDoneTask = new Task();
        notDoneTask.setDescription("notDoneTask");

        Week previuosWeek = new Week();
        previuosWeek.getTasks().add(doneTask);
        previuosWeek.getTasks().add(notDoneTask);

        when(taskService.findUpcomingTasks(any(User.class), anyInt(), anyInt()))
                .thenReturn(List.of(plannedTask));
        when(weekService.findPreviousWeek(any(User.class)))
                .thenReturn(Optional.of(previuosWeek));
        when(weekService.findCurrentWeek(any(User.class)))
                .thenReturn(Optional.empty());
        when(userService.findByUsername(anyString()))
                .thenReturn(testUser);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("TestUser");

        //act
        ResultActions resultActions = mockMvc.perform(get("/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("weekReview"))
                .andExpect(model().attributeExists("user", "previousWeek", "upcomingTasks"));

        //assert
        resultActions.andExpect(content().string(containsString("<p class=\"taskText\">notDoneTask</p>")));
        resultActions.andExpect(content().string(containsString("<p class=\"taskText\">doneTask</p>")));
        resultActions.andExpect(content().string(containsString("<p class=\"taskText\">plannedTask</p>")));
    }
}
