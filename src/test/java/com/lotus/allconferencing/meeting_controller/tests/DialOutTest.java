package com.lotus.allconferencing.meeting_controller.tests;

import com.lotus.allconferencing.BaseSeleniumTest;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Ben on 8/9/2015.
 */
public class DialOutTest extends BaseSeleniumTest {
    private static WebDriver driver = getDriver();
    private String documentName = "new_test_doc";

    public static WebDriver getBrowser(String browserType) {
        if (driver == null) {
            if (browserType.equals("Firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("Chrome")) {
            }
        }
        return driver;
    }

    @Test
    public void dialOut() {

    }
}
