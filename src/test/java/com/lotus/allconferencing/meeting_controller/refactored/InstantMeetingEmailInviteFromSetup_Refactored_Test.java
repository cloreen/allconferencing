package com.lotus.allconferencing.meeting_controller.refactored;

import com.lotus.allconferencing.meeting_controller.pages.GmailPageObject;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.meeting_controller.pages.WindowManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ben on 7/15/2015.
 */
public class InstantMeetingEmailInviteFromSetup_Refactored_Test {
    private static WebDriver driver, driver2;
    private LoginPageObject loginPage;
    private static String baseWindow = "";
    private static String loginWindow = "";
    private static String myAccountWindow = "";
    private static String projectManagerWindow = "";
    private static String meetingControllerWindow = "";

    private WindowManager windowManager = new WindowManager(driver);

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
        //loginPage.get();
        loginPage.selectLogin(LoginPageObject.LoginType.STANDARD);


        myAccountWindow = getWindow();
        System.out.println("My Account window handle is: " + myAccountWindow);
        loginPage.login("25784", "lotus456");
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
    }

    public void instantMeetingQuickSetup() {
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

        WebElement baseElement = driver.findElement(By.cssSelector("html"));
        baseElement.click();

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
        GmailPageObject gmail = new GmailPageObject(driver);
        gmail.get();
        gmail.signIn("bgactest03@gmail.com", "lotus12345");
        String subject = gmail.checkInvite();
        return subject;
    }

    public ExpectedCondition<Boolean> windowIsNotPresent() {
        ExpectedCondition<Boolean> expectedCondition = null;
        Boolean bool = false;
        String currentHandle = driver.getWindowHandle();
        Set<String> currentHandles = driver.getWindowHandles();
        return expectedCondition;
    }

    private class HandleChecker implements ExpectedCondition<Boolean> {
        private String currentWindowHandle;
        private Set<String> currentWindows;

        public HandleChecker(final String currentWindowHandle, final Set<String> currentWindows) {
            this.currentWindowHandle = currentWindowHandle;
            this.currentWindows = currentWindows;
        }

        @Override
        public Boolean apply(WebDriver webDriver) {
            List<String> handles = null;
            for(String window : currentWindows) {
                handles.add(window);
            }
            Boolean windowExists = true;
            int j = 0;

            for (int i = 0; i < 4; i++) {
                for (String handle : handles) {
                    try {
                        if (handle.equals(this.currentWindowHandle)) {
                            j++;
                        }
                        continue;
                    } catch (StaleElementReferenceException e) {
                        // too fast, need to refresh the list so trigger a poll
                        // and wait for next time
                        // do nothing
                    }
                }
                if (j == i) {
                    continue;
                } else if (j < i) {
                    windowExists = false;
                    break;
                }

            }

            return windowExists;
        }
    }

        @Before
        public void setup() {
            driver = new FirefoxDriver();
            driver.get("http://www.allconferencing.com/");
            baseWindow = getWindow();
            getLoginPage(By.cssSelector("ul[id='MenuBar3']>li>a"), 2);
            //standardLogin("25784", "lotus456");
        }

        @Test
        public void inviteFromSetup() {
            instantMeetingQuickSetup();
            driver.switchTo().window(baseWindow);
            String emailReceived = checkInviteEmail();
            assertEquals("Email was received", "AllConferencing Meeting Invite", emailReceived);
        }

        @Test
        public void inviteFromMeetingController() {
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
            WebDriverWait waitForMeetingControllerToCloae = new WebDriverWait(driver, 10);
            waitForMeetingControllerToCloae.until(
                    new HandleChecker(driver.getWindowHandle(), driver.getWindowHandles())
            );

            closeWindow(projectManagerWindow);
            closeWindow(myAccountWindow);
            driver.switchTo().window(baseWindow);

            //driver.quit();
        }
    }

