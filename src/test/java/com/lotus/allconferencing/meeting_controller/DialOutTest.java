package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.webdriver.manager.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 8/9/2015.
 */
public class DialOutTest {
    private WebDriver driver;
    private String baseWindow, myAccountWindow, projectManagerWindow, meetingControllerWindow;
    private LoginPageObject loginPage;
    private Integer inviteFromMeetingFlag = 0;

    public String getWindow() {
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
        //loginPage.get();
        loginPage.selectLogin(loginType);

        myAccountWindow = getWindow();
        loginPage.login("25784", "lotus456");
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
        System.out.println("My Account window handle is: " + myAccountWindow);

    }

    // Sets up an Instant Meeting from Account Landing page and sends an invite based on argument
    // Arguments -
    //      1. Invite from Instant Meeting Setup interface in Project Manager
    //      2. Invite from within Meeting Controller after meeting has started

    public void startInstantMeeting() {
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

        WebElement confirmButton = driver.findElement(By.cssSelector("button[id='amndstart-button']"));
        confirmButton.click();
        confirmButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.textToBePresentInElement(confirmButton, "Start Meeting Now")
        );

        confirmButton.click();

        // Switch to newly opened window
        meetingControllerWindow = getWindow();
        System.out.println("Meeting Controller window handle is: " + meetingControllerWindow);

        WebDriverWait waitForMtgCntrlr = new WebDriverWait(driver, 10);
        waitForMtgCntrlr.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")));

    }

    public void endMeeting() {
        if (meetingControllerWindow != "") {

            driver.switchTo().window(meetingControllerWindow);

            WebElement htmlElement = driver.findElement(By.tagName("html"));
            htmlElement.click();
            //WebDriverWait waitForMtgCntrlsMenu = new WebDriverWait(driver, 10);
            //waitForMtgCntrlsMenu.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")));

            if (inviteFromMeetingFlag == 0) {
                WebElement mtgCtrlsMenu = driver.findElement(By.xpath(".//*[@id='yui-gen37']/a")/*("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")*/); // .//*[@id='yui-gen37']/a
                Actions action = new Actions(driver);
                action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
                action.release().perform();
                WebElement endMtgButton = driver.findElement(By.cssSelector("button[id='genericdualoptdialogopt1-button']"));
                endMtgButton.click();
            } else if (inviteFromMeetingFlag == 1) {
                WebElement mtgCtrlsMenu = driver.findElement(By.xpath("/html/body/div[10]/div/div[2]/div[2]/div/div/div/ul/li")); // .//*[@id='yui-gen37']/a
                Actions action = new Actions(driver);
                action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
                action.release().perform();
                WebElement endMtgButton = driver.findElement(By.cssSelector("button[id='genericdualoptdialogopt1-button']"));
                endMtgButton.click();
            }

            //WebDriverWait wait = new WebDriverWait(driver, 10);

        }
    }


    @Before
    public void setup() {
        Driver.set(Driver.BrowserName.FIREFOX);
        driver = Driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
        getLoginPage(LoginPageObject.LoginType.STANDARD);
        startInstantMeeting();
    }

    @Test
    public void dialOut() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement currentHeaderElement;

        WebElement topMenuBarDialPadOption = driver.findElement(By.xpath("/html/body/div[7]/div[2]/div/table/tbody/tr/td[2]/div/div/ul/li[3]/a"));
        topMenuBarDialPadOption.click();

        WebElement dialPadInput = driver.findElement(By.cssSelector("input[id='numberbox']"));
        dialPadInput.sendKeys(new String("3102103647"));
        WebElement dialPadCallButton = driver.findElement(By.cssSelector("img[id='buttonCall']"));
        dialPadCallButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(currentHeaderElement = driver.findElement(By.xpath("/html/body/div[4]/div/div")), "Dial-out"));

        WebElement dialOutButton = driver.findElement(By.cssSelector("button[id='dialoutbutton-button']"));
        dialOutButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(currentHeaderElement = driver.findElement(By.xpath(".//*[@id='simpleerrordialog']/div[1]")/*("/html/body/div[6]/div[1]/div[1]")*/), "Dial-out Response"));

        WebElement dialOutDialogButton = driver.findElement(By.cssSelector("button[id='simpleerrordialogok-button']"));
        dialOutDialogButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button[id='simpleerrordialogok-button']")));

        // Manually check for call to be received.
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        endMeeting();
        driver.quit();
    }
}
