package com.example.todoz.e2e.prepared_actions;

import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;


import java.util.Random;

@Component
public class TestAction {

    public void RegisterAndLogin(Page page, String webUrl) {

        String username = String.format("testUser%s@mail.com", new Random().nextInt());

        //Register action
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", username);
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        page.waitForTimeout(2000);

        //Login action
        page.type("input[name=username]", username);
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");
    }

    public void createFourNoDueDateTasks(Page page) {
        for (int i = 1; i <= 4; i++) {
            page.type("#add", "NoDueDateTask" + i);
            page.click(".stars button[value='" + i + "']");
        }
    }

    public void createDueDateTask(Page page, String date, int priority, String title) {
        page.type("#add", title);
        page.click("#calendar_icon");
        page.fill("#date", date);
        page.click(".stars button[value='" + priority + "']");
    }
}
