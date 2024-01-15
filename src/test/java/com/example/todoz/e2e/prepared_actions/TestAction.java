package com.example.todoz.e2e.prepared_actions;

import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

@Component
public class TestAction {
    public void RegisterAndLogin(Page page, String webUrl) {
        //Register action
        page.navigate(webUrl + "/register");

        page.type("input[name=username]", "testUser");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");

        if ((webUrl + "/register?userExists=true").equals(page.url())) {
            page.navigate(webUrl + "/login");
            page.type("input[name=username]", "testUser");
            page.type("input[name=password]", "testPassword");
            page.click("button[type=submit]");
        }

        //Login action
        page.type("input[name=username]", "testUser");
        page.type("input[name=password]", "testPassword");
        page.click("button[type=submit]");
    }
    public void createFourNoDueDateTasks(Page page) {
        for (int i = 1; i <= 4; i++) {
            page.type("#createTask-input", "NoDueDateTask" + i);
            page.evaluate("setPriority(" + i + ");");
        }
    }
    public void createDueDateTask(Page page,String date,int priority){
        page.waitForTimeout(2000);
        page.type("#createTask-input", "tomorrowTask");
        page.evaluate("setPriority(" + priority + ");");
        page.click("#calendar_icon");
        page.type("#date",date);
    }

}
