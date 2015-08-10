package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.webdriver.manager.Driver;
import org.junit.Before;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 8/9/2015.
 */
public class DialOutTest {
    private WebDriver driver = Driver.set(Driver.BrowserName.FIREFOX);
    private String baseWindow;

    @Before
    public void setup() {
        driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
    }
}
