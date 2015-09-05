package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.BaseSeleniumTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Ben on 9/1/2015.
 */
public class AddDocumentTest extends BaseSeleniumTest {
    private static WebDriver driver = getDriver();

    public static WebDriver getBrowser(String browserType) {
        if(driver == null) {
            if(browserType.equals("Firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("Chrome")) {
            }
        }
        return driver;
    }

    @Test
    public void addNewDocument() {
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();
        WebElement meetingControlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li"));
        meetingControlsMenu.click();

        WebElement newDocumentMenuOption = driver.findElement(By.xpath("./[@id='yui-gen110']/a"));
        newDocumentMenuOption.click();
    }

}
