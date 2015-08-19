package com.lotus.allconferencing.meeting_controller;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
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


    public void instantMeetingQuickSetupInviteOnePart() {
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
        waitForInstantMeetingSetup.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hd")));

        WebElement partName = driver.findElement(By.cssSelector("input[id='new_meet_part_name']"));
        partName.sendKeys(new String("AutoTest-Gmail"));
        WebElement partEmail = driver.findElement(By.cssSelector("input[id='new_meet_part_email']"));
        partEmail.sendKeys(new String("bgactest03@gmail.com"));
        WebElement addButton = driver.findElement(By.cssSelector("button[id='addmeetpart-button']"));
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.textToBePresentInElement(driver.findElement(By.cssSelector("table tbody:nth-of-type(2) tr td:nth-of-type(3) div div")), "bgactest03@gmail.com")
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

    public String checkInviteGmail() {
        Set<String> windows = driver.getWindowHandles();
        String adminToolHandle = driver.getWindowHandle();
        ((JavascriptExecutor)driver).executeScript("window.open();");
        Set<String> newWindow = driver.getWindowHandles();
        newWindow.removeAll(windows);
        String customerSiteHandle = ((String)newWindow.toArray()[0]);
        driver.switchTo().window(customerSiteHandle);
        driver.get("http://www.gmail.com/");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
        wait.until(ExpectedConditions.titleIs("Gmail"));
        WebElement emailAddress = driver.findElement(By.cssSelector("input[id='Email']"));
        emailAddress.sendKeys(new String("bgactest03@gmail.com"));
        emailAddress.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']")));
        WebElement password = driver.findElement(By.cssSelector("input[id='Passwd']"));
        password.sendKeys(new String("lotus12345"));
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
                if (currentMinutes <= emailMinuteThreshold) {
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

        System.out.println("The subject of the email found is: " + emailSubject.getText());
        return(emailSubject.getText());
    }

    public String checkInviteHotmail() {
        driver.switchTo().window(baseWindow);
        driver.get("http://www.live.com/");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
        wait.until(ExpectedConditions.titleIs("Sign In"));
        WebElement emailAddress = driver.findElement(By.xpath(".//*[@id='idDiv_PWD_UsernameExample']"));
        emailAddress.sendKeys(new String("bgactest02@hotmail.com"));
        WebElement password = driver.findElement(By.xpath(".//*[@id='idDiv_PWD_PasswordExample']"));
        password.sendKeys(new String("lotus1234"));
        WebElement signInButton = driver.findElement(By.xpath(".//*[@id='idSIButton9']"));
        signInButton.click();
        wait.until(ExpectedConditions.titleIs("Outlook.com - bgactest02@hotmail.com"));

        String emailText = "AllConferencing Meeting Invite";
        WebElement refreshButton = driver.findElement(By.xpath(".//*[@id='Refresh']"));
        WebElement emailSubject = null;
        WebElement junkDropDown = driver.findElement(By.xpath(".//*[@id='MarkAsJunk']/span"));

        //.//*[@id='uk0ipu4DJF5RGCadidZ1yJUg2']/span[4]/a
        //.//*[@id='ukl3C-sGhF5RGIgQAhWtlluA2']/span[4]/a
        try {
            //Actions actions = new Actions(driver);
            //actions.moveToElement(junkDropDown).moveByOffset(525, 0).perform();
            emailSubject = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div/div/div/div[2]/div/div/div/div/div/div/div[2]/ul/li/span[4]/a"));
            String emailSubjectString = emailSubject.getText();
            System.out.println("Email Subject is: " + emailSubjectString);
            /*
            WebElement emailArrivalTime = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"));
            String emailTime = emailArrivalTime.getText();
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
                if (currentMinutes <= emailMinuteThreshold) {
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
            } */
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
        invitePartName.sendKeys(new String("AutoTest-Hotmail"));
        WebElement invitePartEmail = driver.findElement(By.cssSelector("input[id='new_invite_part_email'"));
        invitePartEmail.sendKeys(new String("bgactest02@hotmail.com"));
        WebElement addPartButton = driver.findElement(By.cssSelector("button[id='addinvitepart-button']"));
        addPartButton.click();

        WebElement sendInvitesButton = driver.findElement(By.cssSelector("button[id='sendinvites-button']"));
        sendInvitesButton.click();
    }

    @BeforeClass
    public static void setup() {
        driver = new FirefoxDriver();
        driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
        getLoginPage(LoginPageObject.LoginType.STANDARD);
    }

    @Test
    public void sendInviteFromSetup() {
        participantNumber = 1;
        instantMeetingQuickSetupInviteOnePart();
        String emailReceived = checkInviteGmail();
        assertEquals("Email was received", "AllConferencing Meeting Invite", emailReceived);
    }

    @Test
    public void sendInviteFromMeeting() {
        inviteFromMeeting();
        String emailReceived1 = checkInviteHotmail();
        assertEquals("Email was received", "AllConferencing Meeting Invite", emailReceived1);
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
