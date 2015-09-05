package com.lotus.allconferencing.suites;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.SetupInstantMeetingTest;
import com.lotus.allconferencing.meeting_controller.AddDocumentTest;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        SetupInstantMeetingTest.class,
        AddDocumentTest.class
})

public class MeetingControllerSuiteTest {
    public static WebDriver driver;


    @BeforeClass
    public static void setUp() {

        driver = new FirefoxDriver();
        
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
