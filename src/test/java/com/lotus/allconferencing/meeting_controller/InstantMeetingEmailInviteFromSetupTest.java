package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.sun.jna.StringArray;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by Ben on 7/15/2015.
 */
public class InstantMeetingEmailInviteFromSetupTest {
    private static WebDriver driver, driver2;
    private LoginPageObject loginPage;
    private static String baseWindow = "";
    private static String loginWindow = "";
    private static String myAccountWindow = "";
    private static String projectManagerWindow = "";
    private static String meetingControllerWindow = "";

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

    public void getLoginPage(By by, int pos) {

        System.out.println("Base window handle is: " + baseWindow);

        loginPage = new LoginPageObject(driver);
        loginPage.get();
        loginPage.selectLogin(LoginPageObject.LoginType.STANDARD);

/*
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(by, pos)).perform();
            actions.sendKeys(new String("w")).perform();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
        loginPage.login("25784", "lotus456");
        myAccountWindow = getWindow();
        System.out.println("My Account window handle is: " + myAccountWindow);

        /*
        for (String item : set) {
            i++;
            System.out.println("Handle of Window " + i + " is: " + item);
            if (i == 1) {
                myHandle = item.toString();
            } else {
                otherHandle = item.toString();
            }
        }

        //List handleList = (List)set;
        int x = windowHandles.size();
        for (int a=0;a<x-1;a++) {
            windowHandles.remove(a);
        }

        System.out.println("The size of set is " + windowHandles.size());
        if(windowHandles.size() == 1) {
            for (String handle : windowHandles) {
                System.out.println("Remaining handle is " + handle);
                newHandle = handle;
            }
        }
*/


//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[id='lnkDialIn']")));
//        loginPageTitle = aDriver.getTitle();

//        System.out.println(loginPageTitle);
    }

    public void instantMeetingQuickSetup() {
        WebElement instMtgLink = driver.findElement(By.cssSelector("a[id='lnkDialIn']"));
        instMtgLink.click();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement baseElement = driver.findElement(By.cssSelector("html"));
        baseElement.click();


        // Switch to newly opened window
        projectManagerWindow = getWindow();
        System.out.println("Project Manager window handle is: " + projectManagerWindow);
/*
        for (String item : set) {
            i++;
            System.out.println("Handle of Window " + i + " is: " + item);
            if (i == 1) {
                myHandle = item.toString();
            } else {
                otherHandle = item.toString();
            }
        }

        //List handleList = (List)set;
        int x = windowHandles.size();
        for (int a=0;a<x-1;a++) {
            windowHandles.remove(a);
        }

        System.out.println("The size of set is " + windowHandles.size());
        if(windowHandles.size() == 1) {
            for (String handle : windowHandles) {
                System.out.println("Remaining handle is " + handle);
                newHandle = handle;
            }
        }

        driver.switchTo().window(newHandle);

*/
        WebElement newBaseElement = driver.findElement(By.cssSelector("html"));
        newBaseElement.click();

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

        wait.until(
                ExpectedConditions.textToBePresentInElement(confirmButton, "Start Meeting Now")
        );

        confirmButton.click();

        // Switch to newly opened window
        meetingControllerWindow = getWindow();
        System.out.println("Meeting Controller window handle is: " + meetingControllerWindow);
    }

    public String checkInviteEmail() {
        driver.switchTo().window(baseWindow);
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
        refreshButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement emailSubject = driver.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
        String emailSubjectString = emailSubject.getText();
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

        WebDriverWait waitForEmail = new WebDriverWait(driver, 30);
        waitForEmail.until(ExpectedConditions.textToBePresentInElement(emailSubject, emailText));

        System.out.println("The subject of the email found is: " + emailSubject.getText());
        return(emailSubject.getText());
    }

    @Before
    public void setup() {
        driver = new FirefoxDriver();
        driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
        getLoginPage(By.cssSelector("ul[id='MenuBar3']>li>a"), 2);
        //standardLogin("25784", "lotus456");
    }

    @Test
    public void inviteFromSetup() {
        instantMeetingQuickSetup();
        String emailReceived = checkInviteEmail();
        assertEquals("Email was received", "AllConferencing Meeting Invite", emailReceived);
    }

    @After
    public void tearDown() {
        driver.switchTo().window(meetingControllerWindow);
        WebElement mtgCtrlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")); // .//*[@id='yui-gen37']/a
        Actions action = new Actions(driver);
        action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
        action.release().perform();
        WebElement endMtgButton = driver.findElement(By.cssSelector("button[id='genericdualoptdialogopt1-button']"));
        endMtgButton.click();

        closeWindow(projectManagerWindow);
        closeWindow(myAccountWindow);
        driver.switchTo().window(baseWindow);

        //driver.quit();
    }
}
