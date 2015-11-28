package com.lotus.allconferencing.meeting_controller.pages;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.GmailInboxComponentsObject;
import com.lotus.allconferencing.meeting_controller.pages.components.GmailLoginPageObject;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 * TODO - Comment refactor points
 * TODO - Refactor into functions and components
 */
public class GmailObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;
    private static WebElement subject;
    private static WebElement emailArrivalTime;
//    private GmailInboxComponentsObject gmailInbox;
//    private GmailLoginPageObject gmail;


    public GmailObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    GmailLoginPageObject gmail = new GmailLoginPageObject(driver);
    GmailInboxComponentsObject gmailInbox = new GmailInboxComponentsObject(driver);


    public void get() {

        driver.get("http://www.gmail.com/");

    }


    public String checkInviteEmail() {
        // Wait for title
        loginToGmail(gmail);

        // Inbox - get most recent email, evaluate time received and email subject
        String emailText = "AllConferencing Meeting Invite";
        try {
            subject = getSubject(gmailInbox);
            String emailSubjectString = getSubjectText(gmailInbox);;
            WebElement emailArrivalTime = getEmailArrivalTime(gmailInbox);
            String emailTime = emailArrivalTime.getText();
            for (int i = 0; i < 3; i++) {
                if (!emailTime.contains("am")) {
                    if (!emailTime.contains("pm")) {
                        refreshInbox(gmailInbox);
                        emailArrivalTime = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
                        emailTime = emailArrivalTime.getText();
                    }
                }
            }

            System.out.println("Email time retrieved from Gmail was: " + emailTime);
            String[] emailTimeParts = emailTime.split(":");
            String emailHourStr = emailTimeParts[0];
            String emailMinuteStr = emailTimeParts[1];
            String[] minuteParts = emailMinuteStr.split("\\s");
            emailMinuteStr = minuteParts[0];
            Integer emailHour = (Integer.parseInt(emailHourStr));
            System.out.println("Email hour is: " + emailHour);
            Integer emailMinute = (Integer.parseInt(emailMinuteStr));
            System.out.println("Email minute is: " + emailMinute);
            Integer emailMinuteThreshold = emailMinute + 1;
            System.out.println("Email Minute Threshold is: " + emailMinuteThreshold);

            DateTime dt = new DateTime();
            System.out.println("Current DateTime is: " + dt);
            Integer currentHour = dt.getHourOfDay();
            System.out.println("Current Hour is: " + currentHour);
            Integer currentMinutes = dt.getMinuteOfHour();
            System.out.println("Current Minute is: " + currentMinutes);
            if (currentHour > 12) {
                currentHour -= 12;
            }
            if (currentHour == 0) {
                currentHour = 12;
            }

            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.textToBePresentInElement(emailArrivalTime, String.valueOf(currentHour)));
            Boolean thresholdReached = false;
            for (int i = 0; i < 15; i++) {
                if (currentMinutes <= emailMinuteThreshold) {
                    thresholdReached = true;
                    i = 15;
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 6 || i == 12) {
                    refreshInbox(gmailInbox);
                }
            }

            if (thresholdReached == false) {
                System.out.println("Email time synchronization failed. The time threshold was not reached.");
                System.exit(-1);
            }
        } catch (NoSuchElementException nsee) {
            refreshInbox(gmailInbox);
        } catch (ElementNotFoundException enfe) {
            refreshInbox(gmailInbox);
        }


        //WebDriverWait waitForEmail = new WebDriverWait(driver, 30);
        //waitForEmail.until(ExpectedConditions.textToBePresentInElement(emailSubject, emailText));

        System.out.println("The subject of the email found is: " + subject.getText());
        return (subject.getText());

    }

    private void loginToGmail(GmailLoginPageObject gmail) {
        gmail = new GmailLoginPageObject(driver);
        gmail.goToGmail();
        gmail.enterGmailUsername();
        gmail.enterGmailPassword();
    }

    public void getEmail() {
        subject.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebElement getSubject(GmailInboxComponentsObject gmailInbox) {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.getEmailSubject();
    }

    public String getSubjectText(GmailInboxComponentsObject gmailInbox) {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.getEmailSubjectText();
    }

    public WebElement getEmailArrivalTime (GmailInboxComponentsObject gmailInbox) {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.emailArrivalTime();
    }

    public void refreshInbox (GmailInboxComponentsObject gmailInbox) {
        gmailInbox = new GmailInboxComponentsObject(driver);
        gmailInbox.refreshInbox();
    }
}
