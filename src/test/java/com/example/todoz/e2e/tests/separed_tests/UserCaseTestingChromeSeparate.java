package com.example.todoz.e2e.tests.separed_tests;

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
public class UserCaseTestingChromeSeparate {

    @Autowired
    private TestAction actions;

    @Value("${app.url}")
    private String webUrl;
    private static Browser browser;
    private static Page page;

    @BeforeEach
    public void setUp() {
        try {
            browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            page = context.newPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
    }

    @Test
    @Order(1)
    public void userCreateNoDueDateTask() {
        //act
        actions.RegisterAndLogin(page, webUrl);
        page.waitForTimeout(2000);
        actions.createFourNoDueDateTasks(page);
        //assert
        for (int i = 1; i <= 4; i++) {
            String descriptionSelector = String.format(".task:has(p:has-text('NoDueDateTask%d'))", i);

            assertEquals("NoDueDateTask" + i, page.innerText(descriptionSelector));
            assertEquals(String.valueOf(i), page.getAttribute(descriptionSelector, "data-priority"));
        }
    }

    @Test
    @Order(2)
    public void CreateThisWeekDueDateTask() {
        //arrange
        String taskLocator = ".task:has(p:has-text('tomorrow')) .due";
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        int priority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        actions.createDueDateTask(page, tomorrow.toString(), priority, "tomorrow");
        page.waitForTimeout(2000);
        //assert
        assertEquals(tomorrow.toString(), page.getAttribute(taskLocator, "data-duedate"));
    }

    @Test
    @Order(3)
    public void CreateNextWeekDueDateTask() {
        //arrange
        String taskLocator = ".task:has(p:has-text('nextWeek')) .due";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);
        String dayAfterNextWeek = nextWeek.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        String expectedDateFormat = dayAfterNextWeek + " " + nextWeek.format(formatter);
        int priority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        actions.createDueDateTask(page, nextWeek.toString(), priority, "nextWeek");
        page.navigate(webUrl + "/longTerm");
        page.waitForTimeout(2000);
        //assert
        assertEquals(expectedDateFormat, page.innerText(taskLocator));
    }

    @Test
    @Order(4)
    public void CreateNextTwoWeeksDueDateTask() {
        //arrange
        String taskLocator = ".task:has(p:has-text('nextTwoWeeks')) .due";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        LocalDate nextTwoWeeks = today.plusWeeks(2);
        String dayAfterNextTwoWeeks = nextTwoWeeks.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        String expectedDateFormat = dayAfterNextTwoWeeks + " " + nextTwoWeeks.format(formatter);
        int priority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        page.navigate(webUrl + "/");
        actions.createDueDateTask(page, nextTwoWeeks.toString(), priority, "nextTwoWeeks");
        page.navigate(webUrl + "/longTerm");
        page.waitForTimeout(2000);
        //assert
        assertEquals(expectedDateFormat, page.innerText(taskLocator));
    }

    @Test
    @Order(5)
    public void editTaskPriority() {
        //arrange
        String taskBeforeEditLocator = ".task:nth-child(1)";
        String taskEditedTextLocator = ".task:has(p:has-text('editTaskPriority'))";
        int newPriority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        //create new task
        page.type("#add", "testTask");
        page.click(".stars button[value='" + 1 + "']");
        //choosing task to edit
        page.navigate(webUrl + "/");
        page.click(taskBeforeEditLocator);
        //deleting previous text
        page.dblclick("#add");
        page.keyboard().press("Backspace");
        //editing priority
        page.type("#add", "editTaskPriority");
        page.click(".stars button[value='" + newPriority + "']");
        page.waitForTimeout(3000);
        //assert
        assertEquals(String.valueOf(newPriority), page.getAttribute(taskEditedTextLocator, "data-priority"));
    }

    @Test
    @Order(6)
    public void editTaskDescription() {
        //arrange
        String taskBeforeEditLocator = ".task:nth-child(1)";
        String taskEditedTextLocator = ".task:has(p:has-text('editTaskDescription'))";
        int newPriority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        //create new task
        page.type("#add", "testTask");
        page.click(".stars button[value='" + 1 + "']");
        //choosing task to edit
        page.click(taskBeforeEditLocator);
        //deleting previous text
        page.dblclick("#add");
        page.keyboard().press("Backspace");
        //editing previous description
        page.type("#add", "editTaskDescription");
        page.click(".stars button[value='" + newPriority + "']");
        page.waitForTimeout(3000);
        //assert
        assertEquals("editTaskDescription", page.innerText(taskEditedTextLocator));
    }

    @Test
    @Order(7)
    public void editTaskDueDate() {
        //arrange
        String taskBeforeEditLocator = ".task:nth-child(1)";
        String taskEditedTextLocator = ".task:has(p:has-text('editTaskDueDate')) .due";
        LocalDate today = LocalDate.now();
        LocalDate nextTwoDays = today.plusDays(2);
        int newPriority = 4;
        //act
        actions.RegisterAndLogin(page, webUrl);
        //create new task
        page.type("#add", "testTask");
        page.click(".stars button[value='" + 1 + "']");
        //choosing task to edit
        page.click(taskBeforeEditLocator);
        //deleting previous text
        page.dblclick("#add");
        page.keyboard().press("Backspace");
        //editing dueDate
        page.type("#add", "editTaskDueDate");
        page.click("#calendar_icon");
        page.fill("#date", nextTwoDays.toString());
        page.click(".stars button[value='" + newPriority + "']");
        page.waitForTimeout(2000);
        //assert
        assertEquals(nextTwoDays.toString(), page.getAttribute(taskEditedTextLocator, "data-duedate"));
    }
}
