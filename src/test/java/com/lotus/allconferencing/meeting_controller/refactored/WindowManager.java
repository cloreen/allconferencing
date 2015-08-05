package com.lotus.allconferencing.meeting_controller.refactored;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 7/20/2015.
 */
public class WindowManager {
    private WebDriver driver;

    // This brings the newly opened window into focus.
    public String getWindow(Set<String> handles) {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public WindowManager(WebDriver newDriver) {

        driver = newDriver;

    }
}
