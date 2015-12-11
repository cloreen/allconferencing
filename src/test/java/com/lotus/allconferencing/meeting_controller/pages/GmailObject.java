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
 */
public class GmailObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;
    private static WebElement subject;
    private static WebElement emailArrivalTime;
    private static Integer currentHour, currentMinutes, emailMinuteThreshold;


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
        gmailInbox = new GmailInboxComponentsObject(driver);
        // Login to Gmail
        loginToGmail(gmail);

        // Inbox - get most recent email, evaluate time received and email subject
        String emailText = "AllConferencing Meeting Invite";
        try {
            subject = getSubject();
            String emailSubjectString = getSubjectText();;
            WebElement emailArrivalTime = getEmailArrivalTime();
            String originalTimestamp = emailArrivalTime.getText();
            String emailTime = waitForEmailTimestamp(originalTimestamp);
            //System.out.println("Email time retrieved from Gmail was: " + emailTime);

            createCheckforNewEmail(emailTime, emailArrivalTime);

            waitForEmailToBeReceived(emailArrivalTime, currentHour, currentMinutes, emailMinuteThreshold);

        } catch (NoSuchElementException nsee) {
            refreshInbox();
        } catch (ElementNotFoundException enfe) {
            refreshInbox();
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

    public void checkContentForPasscodes() {

    }

    public WebElement getSubject() {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.getEmailSubject();
    }

    public String getSubjectText() {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.getEmailSubjectText();
    }

    public WebElement getEmailArrivalTime () {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.emailArrivalTime();
    }

    public void refreshInbox () {
        gmailInbox = new GmailInboxComponentsObject(driver);
        WebElement refreshButton = gmailInbox.getRefreshButton();
        refreshButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String waitForEmailTimestamp(String emailTime) {
        gmailInbox = new GmailInboxComponentsObject(driver);
        gmailInbox.waitForEmailTimestamp(emailTime);
        return emailTime;
    }

    public void createCheckforNewEmail(String emailTime, WebElement emailArrivalTime) {
        String[] emailTimeParts = emailTime.split(":");
        String emailHourStr = emailTimeParts[0];
        String emailMinuteStr = emailTimeParts[1];
        String[] minuteParts = emailMinuteStr.split("\\s");
        emailMinuteStr = minuteParts[0];

        Integer emailHour = (Integer.parseInt(emailHourStr));
        //System.out.println("Email hour is: " + emailHour);

        Integer emailMinute = (Integer.parseInt(emailMinuteStr));
        //System.out.println("Email minute is: " + emailMinute);

        emailMinuteThreshold = emailMinute + 1;
        //System.out.println("Email Minute Threshold is: " + emailMinuteThreshold);

        DateTime dt = new DateTime();
        //System.out.println("Current DateTime is: " + dt);

        currentHour = getCurrentHour(dt);
        currentMinutes = dt.getMinuteOfHour();
        //System.out.println("Current Hour is: " + currentHour);
        //System.out.println("Current Minute is: " + currentMinutes);
    }

    public Integer getCurrentHour(DateTime dateTime) {
        Integer currentHour = dateTime.getHourOfDay();
        if (currentHour > 12) {
            currentHour -= 12;
        }
        if (currentHour == 0) {
            currentHour = 12;
        }
        return currentHour;
    }

    public void waitForEmailToBeReceived(WebElement emailArrivalTime, Integer currentHour, Integer currentMinutes, Integer emailMinuteThreshold) {
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
                refreshInbox();
            }
        }

        if (thresholdReached == false) {
            //System.out.println("Email time synchronization failed. The time threshold was not reached.");
            System.exit(-1);
        }
    }
}
