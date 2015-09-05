package com.lotus.allconferencing;

import com.lotus.allconferencing.suites.MeetingControllerSuiteTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class BaseSeleniumTest {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        driver = MeetingControllerSuiteTest.getDriver();
        if(driver != null) {
            return driver;
        }

        return new FirefoxDriver();
    }
}
