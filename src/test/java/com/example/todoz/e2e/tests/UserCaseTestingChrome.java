package com.example.todoz.e2e.tests;

import com.example.todoz.e2e.prepared_actions.TestAction;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

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
        page.waitForTimeout(3500);
    }

    @Test
    @Order(2)
    public void CreateThisWeekDueDateTask() {
        LocalDate today = LocalDate.now();
        int priority = 4;
        LocalDate tomorrow = today.plusDays(1);

        actions.createDueDateTask(page, tomorrow.toString(), priority);
        page.waitForTimeout(3500);
    }

    @Test
    @Order(3)
    public void CreateNextWeekDueDateTask() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);
        int priority = 4;

        actions.createDueDateTask(page, nextWeek.toString(), priority);
        page.navigate(webUrl + "/leftBehind");
        page.waitForTimeout(3500);
        page.navigate(webUrl + "/");
    }

    @Test
    @Order(4)
    public void CreateNextTwoWeeksDueDateTask() {
        LocalDate today = LocalDate.now();
        LocalDate nextTwoWeeks = today.plusWeeks(2);
        int priority = 4;

        actions.createDueDateTask(page, nextTwoWeeks.toString(), priority);
        page.navigate(webUrl + "/leftBehind");
        page.waitForTimeout(3500);
        page.navigate(webUrl + "/");
    }

    @Test
    @Order(5)
    public void editTaskPriority() {
        int priority = 2;

        page.click("#task-input");
        page.type("#createTask-input", "editTaskPriority");
        page.evaluate("setPriority(" + priority + ");");
        page.waitForTimeout(3500);
        page.navigate(webUrl + "/");
    }

    @Test
    @Order(6)
    public void editTaskDescription() {
        int priority = 4;

        page.click("#task-input");
        page.type("#createTask-input", "editTaskDescription");
        page.evaluate("setPriority(" + priority + ");");
        page.waitForTimeout(3500);
        page.navigate(webUrl + "/");
    }

    @Test
    @Order(6)
    public void editTaskDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate nextTwoDays = today.plusDays(2);
        int priority = 4;

        page.click("#task-input");
        page.type("#createTask-input", "editTaskDueDate");
        page.click("#calendar_icon");
        page.type("#date",nextTwoDays.toString());
        page.evaluate("setPriority(" + priority + ");");
        page.waitForTimeout(3500);
        page.navigate(webUrl + "/");
    }
}
