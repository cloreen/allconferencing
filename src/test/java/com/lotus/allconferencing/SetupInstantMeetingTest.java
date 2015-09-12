package com.lotus.allconferencing;

import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 9/2/2015.
 */
public class SetupInstantMeetingTest extends BaseSeleniumTest {
    private static WebDriver driver = getDriver();
    private LoginPageObject loginPage;
    private ReadPropertyFile readProps;
    private static String baseWindow;
    private static String myAccountWindow;
    private static String projectManagerWindow;
    private static String meetingControllerWindow;

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


    @Test
    public void setupInstantMeeting() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.get(readProps.getUrl());
        baseWindow = driver.getWindowHandle();
        getLoginPage(LoginPageObject.LoginType.STANDARD);

        setupMeeting();
    }

    private void setupMeeting() {
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

        WebElement partName = driver.findElement(By.cssSelector("input[id='new_meet_part_name']"));
        partName.sendKeys(new String("AutoTest-Gmail"));
        WebElement partEmail = driver.findElement(By.cssSelector("input[id='new_meet_part_email']"));
        partEmail.sendKeys(new String(readProps.getParticipantEmail()));
        WebElement addButton = driver.findElement(By.cssSelector("button[id='addmeetpart-button']"));
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
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

        WebDriverWait waitForStartMeetingButton = new WebDriverWait(driver, 10);
        waitForStartMeetingButton.until(
                ExpectedConditions.textToBePresentInElement(confirmButton, "Start Meeting Now")
        );

        confirmButton.click();

        meetingControllerWindow = getWindow();

        WebDriverWait waitForMeetingController = new WebDriverWait(driver, 10);
        waitForMeetingController.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li"))
        );
    }
}
