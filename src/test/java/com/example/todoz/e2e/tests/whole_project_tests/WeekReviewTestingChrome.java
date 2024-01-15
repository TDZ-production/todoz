package com.example.todoz.e2e.tests.whole_project_tests;

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
public class WeekReviewTestingChrome {

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
    public void showWeekReviewBar() {
        String fakeDate = LocalDate.now().plusWeeks(1).toString() + "T12:00:00";
        //Set fakeDate
        page.evaluate("() => { Date.now = () => " + fakeDate + "; }");
        //Register,login and add new tasks
        actions.RegisterAndLogin(page, webUrl);
        actions.createTasksForAllCases(page, LocalDate.now().plusWeeks(1));
        page.waitForTimeout(2000);
        //Show weekReview
        page.navigate(webUrl + "/");
        page.waitForTimeout(4000);
    }

    @Test
    @Order(2)
    public void createNewWeek() {
        page.click("#create_new_week");
        page.waitForTimeout(4000);
        page.navigate(webUrl + "/");
    }

    @Test
    @Order(3)
    public void leftBehindIsNotEmpty() {
        page.navigate(webUrl + "/leftBehind");
        page.waitForTimeout(4000);
    }
}
