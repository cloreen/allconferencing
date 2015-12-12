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

    // Selectors for Gmail Inbox Page Components--------------------------------------------
    private static final By REFRESH = By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div");
    private static final By TIMESTAMP = By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span");
    //-----------------------------------------------------------------------------------------------


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

    public WebElement getEmailArrivalTime() {
        WebElement emailArrivalTime = driver.findElement(TIMESTAMP);
        return emailArrivalTime;
    }

    public WebElement getRefreshButton() {
        WebElement refreshButton = driver.findElement(REFRESH);
        return refreshButton;
    }

    public String waitForEmailTimestamp(String emailTime) {
        for (int i = 0; i < 3; i++) {
            if (!emailTime.contains("am")) {
                if (!emailTime.contains("pm")) {
                    //refreshInbox();
                    WebElement emailArrivalTime = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
                    emailTime = emailArrivalTime.getText();
                }
            }
        }
        return emailTime;
    }

}
