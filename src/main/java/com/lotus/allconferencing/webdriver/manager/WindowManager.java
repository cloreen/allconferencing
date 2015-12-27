package com.lotus.allconferencing.webdriver.manager;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 8/9/2015.
 */
public class WindowManager {
    private static WebDriver driver;

    public WindowManager(WebDriver newDriver) {

        driver = newDriver;

    }


    public String getWindow() {
        int i = 0;
        String windowHandle = "";

        Set<String> handlesSet = driver.getWindowHandles();
        for (String item : handlesSet) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }
}
