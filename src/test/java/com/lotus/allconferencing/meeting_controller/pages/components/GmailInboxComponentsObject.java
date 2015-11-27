package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.GmailObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 10/18/2015.
 */
public class GmailInboxComponentsObject {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;


    public GmailInboxComponentsObject(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public WebElement getEmailSubject() {
        WebElement emailSubject = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
        return emailSubject;
    }

    public String getEmailSubjectText() {
        WebElement emailSubject = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
        return emailSubject.getText();
    }

    public WebElement emailArrivalTime() {
        WebElement emailArrivalTime = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
        return emailArrivalTime;
    }

}
