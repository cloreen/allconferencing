package com.lotus.allconferencing.webdriver.manager;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 8/9/2015.
 */
public class WindowManager extends Page {
    public WebDriver driver;

    public WindowManager(WebDriver driver) {
        super(driver);
    }


    public String getWindow() {
        int i = 0;
        String windowHandle = "";
/*
        Set<String> winSet = inSet; //driver.getWindowHandles();
        for (String item : inSet) {
            System.out.println("Handle is: " + item);
        }
        List<String> windowHandles = new ArrayList<String>();
        for (String item : winSet) {
            driver.switchTo().window(item);
        }
*/
        Set<String> handlesSet = driver.getWindowHandles();
        List<String> windowHandles = new ArrayList<String>();
        for (String item : handlesSet) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }
}
