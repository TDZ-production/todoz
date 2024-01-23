package com.example.todoz.e2e.tests.separed_tests;

import com.example.todoz.e2e.prepared_actions.TestAction;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.example.todoz.e2e.prepared_actions")
public class RegisterAndLoginTestingChrome {

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
    public void RegisterWithNewUser() {
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "test@User");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/login", page.url());
    }

    @Test
    @Order(2)
    public void RegisterWithAlreadyCreateUsername() {
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "test@email.com");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "test@email.com");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/register", page.url());
    }

    @Test
    @Order(3)
    public void RegisterWithLowerThanFiveCharactersPassword() {
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "test@email.com");
        page.type("input[name=password]", "1234");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/register", page.url());
    }

    @Test
    @Order(4)
    public void LoginWithNotRegisteredUser() {
        page.navigate(webUrl + "/login");

        page.type("input[name=username]", "test@email.com");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/login", page.url());
    }

    @Test
    @Order(5)
    public void RegisterWithBlankUserName() {
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "  @  ");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/register", page.url());
    }

    @Test
    @Order(6)
    public void RegisterWithBlankPassword() {
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "testU@ser");
        page.type("input[name=password]", "          ");
        page.click("button[type=submit]");

        assertEquals(webUrl + "/register", page.url());
    }
}
