package com.lotus.allconferencing.support_classes;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Ben on 10/18/2015.
 */
public class GmailInboxComponentsObject {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Gmail Inbox Page Components--------------------------------------------
    private static final By EMAIL_SUBJECT = By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span");
    private static final By TIMESTAMP = By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span");
    private static final By REFRESH = By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div");
    private static final By EMAIL_BODY = By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/table/*");
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
        WebElement emailSubject = driver.findElement(EMAIL_SUBJECT);
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

    public List<WebElement> getEmailBody() {
        List<WebElement> emailBody = driver.findElements(EMAIL_BODY);
        return emailBody;
    }
}
