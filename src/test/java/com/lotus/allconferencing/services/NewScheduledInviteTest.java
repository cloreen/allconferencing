package com.lotus.allconferencing.services;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.AddDocPageObject;
import com.lotus.allconferencing.meeting_controller.pages.GmailObject;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.services.meetingmanager.MeetingManagerPageObject;
import com.lotus.allconferencing.suites.MeetingControllerSuiteTest;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.Chronology;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 9/1/2015.
 */
//TODO - Refactor Gmail test into page object
public class NewScheduledInviteTest extends BaseSeleniumTest {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private MeetingManagerPageObject meetingManagerPage;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static String meetingManagerWindow;
    private static Boolean isNewEmail = false;
    private static Pattern pattern = null;


    public WebDriver getBrowser(String browserType) {
        if (driver == null) {
            if (browserType.equals("firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("chrome")) {
            }
        }
        return driver;
    }

    public static String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public void getLoginPage(LoginPageObject.LoginType loginType) {
        System.out.println("Base window handle is: " + baseWindow);

        loginPage = new LoginPageObject(driver);
        loginPage.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPage.login(readProps.getOwnerClientID(), readProps.getOwnerPassword());
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
        System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public void scheduleMeeting() {

        // Open Meeting Manager
        WebElement meetingMgrLink = driver.findElement(By.xpath(".//*[@id='lnkMM']"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        meetingMgrLink.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Transfer driver to new window, bring Meeting Manager window to the foreground,
        // get its handle.
        meetingManagerWindow = getWindow();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.manage().window().maximize();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait waitForMeetingManager = new WebDriverWait(driver, 10);
        waitForMeetingManager.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='zb__App__Calendar_title']"))
        );

        meetingManagerPage = new MeetingManagerPageObject(driver);
        meetingManagerPage.scheduleMeeting();

    }
/*
    public void getGmailInbox () {
        driver2 = new FirefoxDriver();
        gmail.checkInviteEmail();
    }
*/
    public String checkInviteEmail() {
        driver2 = new FirefoxDriver();
        driver2.get("http://www.gmail.com/");
        WebDriverWait wait = new WebDriverWait(driver2, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
        wait.until(ExpectedConditions.titleIs("Gmail"));
        WebElement emailAddress = driver2.findElement(By.cssSelector("input[id='Email']"));
        emailAddress.sendKeys(new String(readProps.getParticipantEmail()));
        emailAddress.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']")));
        WebElement password = driver2.findElement(By.cssSelector("input[id='Passwd']"));
        password.sendKeys(new String(readProps.getParticipantEmailPwd()));
        password.submit();
        wait.until(ExpectedConditions.titleContains("Inbox"));

        String emailText = "AllConferencing Meeting Invite";
        WebElement refreshButton = driver2.findElement(By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div")); // div[role='button']   // div[class='asa']
        WebElement emailSubject = null;
        try {
            emailSubject = driver2.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
            String emailSubjectString = emailSubject.getText();
            WebElement emailArrivalTime = driver2.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
            String emailTime = emailArrivalTime.getText();
            for (int i = 0; i < 3; i++) {
                if (!emailTime.contains("am")) {
                    if (!emailTime.contains("pm")) {
                        refreshButton.click();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        emailArrivalTime = driver2.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
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
                    refreshButton.click();
                }
            }

            if (thresholdReached == false) {
                System.out.println("Email time synchronization failed. The time threshold was not reached.");
                System.exit(-1);
            }
        } catch (NoSuchElementException nsee) {
            refreshButton.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (ElementNotFoundException enfe) {
            refreshButton.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //WebDriverWait waitForEmail = new WebDriverWait(driver, 30);
        //waitForEmail.until(ExpectedConditions.textToBePresentInElement(emailSubject, emailText));

        System.out.println("The subject of the email found is: " + emailSubject.getText());
        return (emailSubject.getText());
    }

    public void verifyEmailReceived(String emailSubject) {
        assertThat("Appropriate email is received", emailSubject.contentEquals("Your conference invitation"));
    }

    public void verifyTollFreeNumberIsGenerated(String tollFreeNum) {
        assertTrue("Toll-Free Number has been generated", tollFreeNum.matches("^?[0-9, -]{1,14}"));
    }

    public void verifyPasscodesAreGenerated(String participantPasscode) {
        assertTrue("Passcodes have been generated", participantPasscode.matches("^?[0-9]{1,6}"));
    }

    public MeetingManagerPageObject meetingManager = new MeetingManagerPageObject(driver);


    @Test
    public void scheduleInMeetingManager() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver = new FirefoxDriver();
        driver.get(readProps.getUrl());

        // Click on HTML element -- May be necessary to run tests in Firefox.
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        // Get handle for home page
        baseWindow = driver.getWindowHandle();

        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        getLoginPage(LoginPageObject.LoginType.STANDARD);

        scheduleMeeting();

    }


    @Test
    public void checkEmailAndPasscodes() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check invite email, passcodes and dial-in numbers have been generated
//        getGmailInbox();
        String inviteEmailSubject = checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);

        WebElement emailSubject = driver2.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
        emailSubject.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> emailBodyTable = driver2.findElements(By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/table/*"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through first wait - table not populated");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through second wait - table still not populated");
            try {
                Thread.sleep(5000);
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
                String tollFreeNumArr[] = tollFreeNum.split(" ");
                String passcodeArr[] = modPasscode.split(" ");
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
                System.out.println("This is index (3) in tollFreeNumArr: " + tollFreeNumArr[3]);
                verifyTollFreeNumberIsGenerated(tollFreeNumArr[3]);
            } else {
                System.out.println("The new email was not found.");
                System.exit(-1);
            }
        }
    }
}


