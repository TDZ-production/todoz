package com.example.todoz.e2e.tests.whole_project_tests;

import com.example.todoz.e2e.prepared_actions.TestAction;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.example.todoz.e2e.prepared_actions")
public class UserCaseTestingChrome {

    @Autowired
    private TestAction actions;

    @Value("${app.url}")
    private String webUrl;
    private static Browser browser;
    private static Page page;

    @BeforeAll
    public static void setUp() {
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            page = context.newPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (browser != null) {
            browser.close();
        }
    }

    @Test
    @Order(1)
    public void userCreateNoDueDateTask() {
        actions.RegisterAndLogin(page, webUrl);
        page.waitForTimeout(2000);
        actions.createFourNoDueDateTasks(page);

        for (int i = 1; i <= 4; i++) {
            String descriptionSelector = String.format(".task:has(p:has-text('NoDueDateTask%d'))", i);

            assertEquals("NoDueDateTask" + i, page.innerText(descriptionSelector));
            assertEquals(String.valueOf(i), page.getAttribute(descriptionSelector, "data-priority"));
        }
    }

    @Test
    @Order(2)
    public void CreateThisWeekDueDateTask() {
        LocalDate today = LocalDate.now();
        int priority = 4;
        LocalDate tomorrow = today.plusDays(1);

        actions.createDueDateTask(page, tomorrow.toString(), priority, "tomorrow");
        page.waitForTimeout(2000);

        assertEquals(tomorrow.toString(), page.getAttribute(".task:has(p:has-text('tomorrow')) .due", "data-duedate"));
    }

    @Test
    @Order(3)
    public void CreateNextWeekDueDateTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);
        int priority = 4;

        actions.createDueDateTask(page, nextWeek.toString(), priority, "nextWeek");
        page.navigate(webUrl + "/longTerm");
        page.waitForTimeout(2000);

        assertEquals(nextWeek.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault())  + " " +
                nextWeek.format(formatter), page.innerText(".task:has(p:has-text('nextWeek')) .due"));
    }

    @Test
    @Order(4)
    public void CreateNextTwoWeeksDueDateTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        LocalDate nextTwoWeeks = today.plusWeeks(2);
        int priority = 4;

        page.navigate(webUrl + "/");
        actions.createDueDateTask(page, nextTwoWeeks.toString(), priority, "nextTwoWeeks");
        page.navigate(webUrl + "/longTerm");
        page.waitForTimeout(2000);

        assertEquals(nextTwoWeeks.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " +
                nextTwoWeeks.format(formatter), page.innerText(".task:has(p:has-text('nextTwoWeeks')) .due"));
    }

    @Test
    @Order(5)
    public void editTaskPriority() {
        int priority = 1;

        page.navigate(webUrl + "/");
        page.click(".task:nth-child(1)");
        page.dblclick("#createTask-input");
        page.keyboard().press("Backspace");
        page.type("#createTask-input", "editTaskPriority");
        page.click(".stars button[value='" + priority + "']");
        page.waitForTimeout(3000);

        assertEquals(String.valueOf(priority), page.getAttribute(".task:has(p:has-text('editTaskPriority'))", "data-priority"));
    }

    @Test
    @Order(6)
    public void editTaskDescription() {
        int priority = 2;

        page.click(".task:nth-child(2)");
        page.dblclick("#createTask-input");
        page.keyboard().press("Backspace");
        page.type("#createTask-input", "editTaskDescription");
        page.click(".stars button[value='" + priority + "']");
        page.waitForTimeout(3000);

        assertEquals("editTaskDescription", page.innerText(".task:has(p:has-text('editTaskDescription'))"));
    }

    @Test
    @Order(7)
    public void editTaskDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate nextTwoDays = today.plusDays(2);
        int priority = 4;

        page.click(".task:nth-child(3)");
        page.dblclick("#createTask-input");
        page.keyboard().press("Backspace");
        page.type("#createTask-input", "editTaskDueDate");
        page.click("#calendar_icon");
        page.fill("#date", nextTwoDays.toString());
        page.click(".stars button[value='" + priority + "']");
        page.waitForTimeout(2000);

        assertEquals(nextTwoDays.toString(), page.getAttribute(".task:has(p:has-text('editTaskDueDate')) .due", "data-duedate"));
    }
}
