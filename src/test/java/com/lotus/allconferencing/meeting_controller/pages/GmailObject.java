package com.lotus.allconferencing.meeting_controller.pages;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 7/20/2015.
 */
public class GmailObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;


    public GmailObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get() {

        driver.get("http://www.gmail.com/");

    }

    public String checkInviteEmail() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
        wait.until(ExpectedConditions.titleIs("Gmail"));
        WebElement emailAddress = driver.findElement(By.cssSelector("input[id='Email']"));
        emailAddress.sendKeys(new String(readProps.getParticipantEmail()));
        emailAddress.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']")));
        WebElement password = driver.findElement(By.cssSelector("input[id='Passwd']"));
        password.sendKeys(new String(readProps.getParticipantEmailPwd()));
        password.submit();
        wait.until(ExpectedConditions.titleContains("Inbox"));

        String emailText = "AllConferencing Meeting Invite";
        WebElement refreshButton = driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div")); // div[role='button']   // div[class='asa']
        WebElement emailSubject = null;
        try {
            emailSubject = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
            String emailSubjectString = emailSubject.getText();
            WebElement emailArrivalTime = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
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
}
