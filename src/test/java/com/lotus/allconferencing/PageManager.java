package com.lotus.allconferencing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class PageManager {
    private static WebDriver driver;
    private static Boolean isInvalid;

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

    public static WebDriverWait waitForTitle(WebDriver pgMgrDriver) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
        waitForTitle.until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
        );

        return waitForTitle;
    }

    public static FluentWait waitForTitleFluently(WebDriver pgMgrDriver) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        FluentWait waitForTitleFluently = new WebDriverWait(driver, 10);
        waitForTitleFluently.
                pollingEvery(100, TimeUnit.MILLISECONDS).
                withTimeout(5, TimeUnit.SECONDS).
                until(
                        ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
                );

        return waitForTitleFluently;
    }

}
