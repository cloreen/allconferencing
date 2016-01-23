package com.lotus.allconferencing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class PageManager {
    private static WebDriver driver;

    public PageManager() {
    }

    public static String getNewWindow(WebDriver winMgrDriver, Set<String> windowHandles) {
        WebDriver driver = winMgrDriver;
        Set<String> set = windowHandles;
        String windowHandle = "";
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public static WebDriverWait waitForTitle(WebDriver pgMgrDriver) {
        WebDriver driver = pgMgrDriver;
        WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
        waitForTitle.until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
        );
        return waitForTitle;
    }

}
