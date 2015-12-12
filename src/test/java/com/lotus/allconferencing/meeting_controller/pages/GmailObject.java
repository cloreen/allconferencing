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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

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
    private static Pattern pattern = null;
    private static Boolean isNewEmail = false;
    private static String tollFreeNumArr[];
    private static String passcodeArr[];
    private static String partPasscode = "";


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
        try {
            subject = getSubject();
            WebElement emailArrivalTime = getEmailArrivalTime();
            String originalTimestamp = emailArrivalTime.getText();
            String emailTime = waitForEmailTimestamp(originalTimestamp);
            //System.out.println("Email time retrieved from Gmail was: " + emailTime);

            createCheckforNewEmail(emailTime);

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

    public void openEmail() {
        subject.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebElement getSubject() {
        gmailInbox = new GmailInboxComponentsObject(driver);
        return gmailInbox.getEmailSubject();
    }

    public WebElement getEmailArrivalTime () {
        gmailInbox = new GmailInboxComponentsObject(driver);
        WebElement emailArrivalTime = gmailInbox.getEmailArrivalTime();
        return emailArrivalTime;
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
        for (int i = 0; i < 3; i++) {
            if (!emailTime.contains("am")) {
                if (!emailTime.contains("pm")) {
                    WebElement emailArrivalTime = getEmailArrivalTime();
                    emailTime = emailArrivalTime.getText();
                }
            }
        }
        return emailTime;
    }

    public void createCheckforNewEmail(String emailTime) {
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

    public void checkEmailContentForNewConfInfo() {
        List<WebElement> emailBodyTable = gmailInbox.getEmailBody();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through first wait - table not populated");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through second wait - table still not populated");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (emailBodyTable.size() != 1) {
            System.out.println("Through third wait (15 seconds) - table still not populated");
        }

        Integer elementCounter = 0;
        String[] emailContent = null;
        System.out.println(emailBodyTable.size());
        DateTime dateTime = new DateTime();
        Integer mins = dateTime.getMinuteOfHour();
        System.out.println("Minute of Hour using DateTime: " + mins);
        Integer minThreshold = null;
        if (mins != 0) {
            minThreshold = mins - 1;
        } else {
            minThreshold = 59;
        }
        System.out.println("Minutes threshold = " + minThreshold);
        String tollFreeNum = null;
        String modPasscode = null;
        for (WebElement element : emailBodyTable) {
            if (element.isDisplayed()) {
                emailContent = element.getText().split("\n");
            }
            for (String line : emailContent) {
                elementCounter += 1;
                System.out.println("Line " + elementCounter + ":");
                System.out.println(line);
                if (line.contains("(0 minutes ago)")) {
                    System.out.println("Found newest email");
                    isNewEmail = true;
                }
                if (isNewEmail == true) {
                    if (line.contains("Toll-free phone number")) {
                        System.out.println("Toll-free number found!");
                        tollFreeNum = line;
                    } else if (line.contains("Participant Passcode")) {
                        System.out.println("Passcode found!");
                        modPasscode = line;
                    }
                }
                System.out.println("");
            }

            if (isNewEmail == true) {
                tollFreeNumArr = tollFreeNum.split(" ");
                passcodeArr = modPasscode.split(" ");
                System.out.println("Toll Free Number line:");
                for (String item : tollFreeNumArr) {
                    System.out.println(item);
                }
                System.out.println("");
                System.out.println("Passcode line:");
                for (String item : passcodeArr) {
                    System.out.println(item);
                }
                pattern = Pattern.compile("\\d+");
                System.out.println("This is index (27) in tollFreeNumArr: " + tollFreeNumArr[27]);
                verifyTollFreeNumberIsGenerated(tollFreeNumArr[27]);
                System.out.println("This is index (28) in participantPasscode: " + passcodeArr[28]);
                verifyPasscodesAreGenerated(passcodeArr[28]);
                partPasscode = passcodeArr[28];
            } else {
                System.out.println("The new email was not found.");
                System.exit(-1);
            }
        }

    }

    public void verifyTollFreeNumberIsGenerated(String tollFreeNum) {
        assertTrue("Toll-Free Number has been generated", tollFreeNum.matches("^?[0-9, -]{1,14}"));
    }

    public void verifyPasscodesAreGenerated(String participantPasscode) {
        assertTrue("Passcodes have been generated", participantPasscode.matches("^?[0-9]{1,6}"));
    }
}
