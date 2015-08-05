package com.lotus.allconferencing.meeting_controller.refactored;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 7/22/2015.
 */
public class GmailPageObject {
    private WebDriver driver;
    private Integer emailHour, emailMinute, emailMinuteThreshold, currentHour, currentMinutes;

    public GmailPageObject(WebDriver newDriver) {

        driver = newDriver;

    }

    public void get() {

        driver.get("http://www.gmail.com/");

    }

    @FindBy(how = How.XPATH, using = "/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div")
    private WebElement refreshButton;

    @FindBy(how = How.CSS, using = "table[id=':36'] tbody tr td:nth-of-type(6) div div div span")
    private WebElement emailSubject;

    @FindBy(how = How.CSS, using = "table[id=':36'] tbody tr td:nth-of-type(8) span")
    private WebElement emailTime;




    public void signIn(String username, String password) {
        WebDriverWait waitForSignInPage = new WebDriverWait(driver, 10);
        waitForSignInPage.until(signInDisplays());
        WebElement emailAddress = driver.findElement(By.cssSelector("input[id='Email']"));
        emailAddress.sendKeys(username);
        emailAddress.submit();
        waitForSignInPage.until(passwordDisplays());
        WebElement pass = driver.findElement(By.cssSelector("input[id='Passwd']"));
        pass.sendKeys(new String("lotus12345"));
        pass.submit();
        WebDriverWait waitForInbox = new WebDriverWait(driver, 10);
        waitForInbox.until(inboxDisplays());
    }

    public String checkInvite() {
        String expectedSubject = "AllConferencing Meeting Invite";

        // Refresh inbox
        WebElement refresh = driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div"));
        refresh.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get subject of latest email in inbox
        String emailSubjectString = emailSubject.getText();

        // Get arrival time of latest email in inbox
        String emailTimeString = emailTime.getText();
        String[] emailTimeParts;
        emailTimeParts = emailTimeString.split(":");
        String emailHourStr = emailTimeParts[0];
        String emailMinuteStr = emailTimeParts[1];
        String[] minuteParts = emailMinuteStr.split("\\s");
        emailMinuteStr = minuteParts[0];
        emailHour = (Integer.parseInt(emailHourStr));
        //System.out.println("Email hour is: " + emailHour);
        emailMinute = (Integer.parseInt(emailMinuteStr));
        //System.out.println("Email minute is: " + emailMinute);

        // Make sure arrival time of most recent email is within a minute of now (so a previous invite email won't be
        // mistaken for the one we're looking for
        emailMinuteThreshold = emailMinute + 1;
        //System.out.println("Email Minute Threshold is: " + emailMinuteThreshold);
        DateTime dt = new DateTime();
//        System.out.println("Current DateTime is: " + dt);
        currentHour = dt.getHourOfDay();
//        System.out.println("Current Hour is: " + currentHour);
        currentMinutes = dt.getMinuteOfHour();
//        System.out.println("Current Minute is: " + currentMinutes);
        if(currentHour > 12) {
            currentHour -= 12;
        }

        // This is a little wait followed by a DIY wait, which
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElement(emailTime, String.valueOf(currentHour)));

        Boolean threshold = false;
        waitForTimeThreshold(threshold);

        WebDriverWait waitForEmail = new WebDriverWait(driver, 30);
        waitForEmail.until(ExpectedConditions.textToBePresentInElement(emailSubject, expectedSubject));

        System.out.println("The subject of the email found is: " + emailSubject.getText());
        return (emailSubject.getText());

    }

    public void waitForTimeThreshold(Boolean threshIn) {
        Boolean thresholdReached = threshIn;
        for(int i = 0; i < 10; i++) {
            if (currentMinutes <= emailMinuteThreshold) {
                thresholdReached = true;
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 6) {
                refreshButton.click();
            }
        }

        if (thresholdReached == false) {
            System.out.println("Email time synchronization failed. The time threshold was not reached.");
            System.exit(-1);
        }
    }

    private static ExpectedCondition<Boolean> signInDisplays() {
        ExpectedCondition<Boolean> expectedCondition = null;
        return expectedCondition = ExpectedConditions.titleIs("Gmail");
    }

    private static ExpectedCondition<WebElement> passwordDisplays() {
        ExpectedCondition<WebElement> expectedCondition = null;
        return expectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']"));
    }

    private static ExpectedCondition<Boolean> inboxDisplays() {
        ExpectedCondition<Boolean> expectedCondition = null;
        return expectedCondition = ExpectedConditions.titleContains("Inbox");
    }
}
