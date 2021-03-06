package com.lotus.allconferencing.meeting_controller.tests;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import com.lotus.allconferencing.webdriver.manager.WindowManager;
import org.joda.time.DateTime;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ben on 7/15/2015.
 */
public class MeetingInvitesPresentationTest {
    private static WebDriver driver, driver2;
    private LoginPageObject loginPage;
    private static WindowManager windowManager;
    private static String baseWindow = "";
    private static String loginWindow = "";
    private static String myAccountWindow = "";
    private static String projectManagerWindow = "";
    private static String meetingControllerWindow = "";
    private static String gmailWindow = "";
    private Integer participantNumber = 0;

    public static WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
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



    public static void closeWindow(String window) {
        driver.switchTo().window(window);
        driver.close();
    }

    public static void getLoginPage(LoginPageObject.LoginType loginType) {
        System.out.println("Base window handle is: " + baseWindow);

        LoginPageObject loginPage = new LoginPageObject(driver);
        loginPage.selectLogin(loginType);

        myAccountWindow = getWindow();
        loginPage.login("25784", "lotus456");
        System.out.println("My Account window handle is: " + myAccountWindow);
    }


    public static void startInstantMeeting() {
        // Sets up an Instant Meeting from Account Landing page and sends an invite to one participant

        WebElement instMtgLink = driver.findElement(By.cssSelector("a[id='lnkDialIn']"));
        instMtgLink.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Switch to newly opened window
        projectManagerWindow = getWindow();
        System.out.println("Project Manager window handle is: " + projectManagerWindow);

        WebElement newBaseElement = driver.findElement(By.cssSelector("html"));
        newBaseElement.click();
        WebDriverWait waitForInstantMeetingSetup = new WebDriverWait(driver, 5);
        waitForInstantMeetingSetup.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='new_meet_part_name']")));

        WebElement confirmButton = driver.findElement(By.cssSelector("button[id='amndstart-button']"));
        confirmButton.click();
        confirmButton.click();

        WebDriverWait waitForStartMeeting = new WebDriverWait(driver, 10);
        waitForStartMeeting.until(
                ExpectedConditions.textToBePresentInElement(confirmButton, "Start Meeting Now")
        );

        confirmButton.click();

        // Switch to newly opened window
        meetingControllerWindow = getWindow();
        System.out.println("Meeting Controller window handle is: " + meetingControllerWindow);
    }

    public String checkInviteGmail(String user, String pass) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        if (participantNumber == 1) {
            Set<String> windows = driver.getWindowHandles();
            String adminToolHandle = driver.getWindowHandle();
            ((JavascriptExecutor)driver).executeScript("window.open();");
            Set<String> newWindow = driver.getWindowHandles();
            newWindow.removeAll(windows);
            String customerSiteHandle = ((String)newWindow.toArray()[0]);
            gmailWindow = customerSiteHandle;
            driver.switchTo().window(gmailWindow);
            driver.get("http://www.gmail.com/");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
            wait.until(ExpectedConditions.titleIs("Gmail"));
        } else {
            WebElement addAccountButton = driver.findElement(By.cssSelector("#account-chooser-add-account"));
            addAccountButton.click();
        }

        WebElement emailAddress = driver.findElement(By.cssSelector("input[id='Email']"));
        emailAddress.sendKeys(new String(user));
        emailAddress.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']")));
        WebElement password = driver.findElement(By.cssSelector("input[id='Passwd']"));
        password.sendKeys(new String(pass));
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
            if(currentHour > 12) {
                currentHour -= 12;
            }

            wait.until(ExpectedConditions.textToBePresentInElement(emailArrivalTime, String.valueOf(currentHour)));
            Boolean thresholdReached = false;
            for(int i = 0; i < 10; i++) {
                if (currentMinutes >= emailMinuteThreshold) {
                    thresholdReached = true;
                    i = 10;
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

        participantNumber = 2;

        System.out.println("The subject of the email found is: " + emailSubject.getText());
        return(emailSubject.getText());
    }




    public void inviteFromMeeting() {
        driver.switchTo().window(meetingControllerWindow);

        WebElement mtgCtrlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")); // .//*[@id='yui-gen37']/a
        Actions action = new Actions(driver);
        action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 32).clickAndHold().perform();
        action.release().perform();

        WebElement invitePartName = driver.findElement(By.cssSelector("input[id='new_invite_part_name'"));
        invitePartName.sendKeys(new String("AutoTest-Gmail"));
        WebElement invitePartEmail = driver.findElement(By.cssSelector("input[id='new_invite_part_email'"));
        invitePartEmail.sendKeys(new String("bgactest05@gmail.com"));
        WebElement addPartButton = driver.findElement(By.cssSelector("button[id='addinvitepart-button']"));
        addPartButton.click();

        WebElement sendInvitesButton = driver.findElement(By.cssSelector("button[id='sendinvites-button']"));
        sendInvitesButton.click();
    }

    @BeforeClass
    public static void setupMeeting() {
        driver = new FirefoxDriver();
        driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
        getLoginPage(LoginPageObject.LoginType.STANDARD);
        startInstantMeeting();
    }

    @Test
    public void sendInviteFromMeeting() {
        inviteFromMeeting();
        if (participantNumber == 2)
            signOutOfGmail();
        String emailReceived1 = checkInviteGmail("bgactest05@gmail.com", "lotus12345");
        assertEquals("Email was received", "AllConferencing Meeting Invite", emailReceived1);
    }

    private void signOutOfGmail() {
        driver.switchTo().window(gmailWindow);
        WebElement acctDropDown = driver.findElement(By.cssSelector(".gb_b.gb_Na.gb_R.gb_Ja"));
        acctDropDown.click();
        WebElement signOutButton = driver.findElement(By.cssSelector("#gb_71"));
        signOutButton.click();
    }

    @AfterClass
    public static void tearDown() {
        if (meetingControllerWindow != "") {

            driver.switchTo().window(meetingControllerWindow);

            WebElement htmlElement = driver.findElement(By.tagName("html"));
            htmlElement.click();
            //WebDriverWait waitForMtgCntrlsMenu = new WebDriverWait(driver, 10);
            //waitForMtgCntrlsMenu.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")));
            try {
                WebElement mtgCtrlsMenu = driver.findElement(By.xpath(".//*[@id='yui-gen37']/a")); // /html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li
                Actions action = new Actions(driver);
                action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
                action.release().perform();
                WebElement endMtgButton = driver.findElement(By.cssSelector("button[id='genericdualoptdialogopt1-button']"));
                endMtgButton.click();
            } catch (ElementNotFoundException enfe) {
                WebElement mtgCtrlsMenu = driver.findElement(By.xpath(".//*[@id='yui-gen37']/a")); // /html/body/div[10]/div/div[2]/div[2]/div/div/div/ul/li
                Actions action = new Actions(driver);
                action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
                action.release().perform();
                WebElement endMtgButton = driver.findElement(By.cssSelector("button[id='genericdualoptdialogopt1-button']"));
                endMtgButton.click();
            }
        }
/*
        closeWindow(projectManagerWindow);
        closeWindow(myAccountWindow);
        driver.switchTo().window(baseWindow);
*/
        driver.quit();
    }
}
