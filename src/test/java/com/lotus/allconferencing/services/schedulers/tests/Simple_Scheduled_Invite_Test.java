package com.lotus.allconferencing.services.schedulers.tests;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.SimpleAccountServicesPage;
import com.lotus.allconferencing.services.schedulers.pages.SimpleScheduledInvitePage;
import com.lotus.allconferencing.support_classes.GmailObject;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 12/17/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Simple_Scheduled_Invite_Test {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPageObject;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    public static String timeOfDay = "";
    public static String partPasscode = "";

    SimpleAccountServicesPage simpleAccountServicesPage = new SimpleAccountServicesPage(driver);
    SimpleScheduledInvitePage simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
    GmailObject gmail = new GmailObject(driver);

    @Test
    public void test1_sendEasyAllInvite() {

        getSettings();
        openBrowser();

        goToHomePage();

        login(LoginPageObject.LoginType.STANDARD);
        openScheduler();
        timeOfDay = selectMeetingHour(timeOfDay);
        selectTimeOfDay(timeOfDay);
        selectTimeZone();
        addParticipant();
        enterMeetingName();
        submitInvite();
    }

    @Test
    public void test2_checkEmailAndPasscodes() {
        getSettings();

        // Check invite email, passcodes and dial-in numbers have been generated
        checkEmailIsReceived();
        gmail.openEmail();
        partPasscode = checkEmailContentForNewConfInfo();
        closeWindow();
    }

    // Verifications ---------------------------------------------------------------------------------------------------

    public void verifyEmailReceived(String emailSubject) {
        assertThat("Appropriate email is received", emailSubject.contentEquals(readProps.getInviteEmailSubject()));
    }

    // End Verifications -----------------------------------------------------------------------------------------------



    public void getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openBrowser() {
        driver = new FirefoxDriver();
    }

    public void goToHomePage() {
        driver.get(readProps.getUrl());

        // Click on HTML element -- May be necessary to run tests in Firefox.
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        // Get handle for home page
        baseWindow = driver.getWindowHandle();
    }

    public static String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        //List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public void login(LoginPageObject.LoginType loginType) {
        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        //System.out.println("Base window handle is: " + baseWindow);

        loginPageObject = new LoginPageObject(driver);
        loginPageObject.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPageObject.login(readProps.getOldAcctClientID(), readProps.getOldAcctPassword());

        //System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public void openScheduler() {
        simpleAccountServicesPage = new SimpleAccountServicesPage(driver);
        simpleAccountServicesPage.openEasyAllInvitePage();
    }

    public String selectMeetingHour(String timeOfDay) {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        timeOfDay = simpleScheduledInvitePage.selectMeetingHour(timeOfDay);
        return timeOfDay;
    }

    public void selectTimeOfDay(String timeOfDay) {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.selectTimeOfDay(timeOfDay);
    }

    public void selectTimeZone() {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.choosePacificTimeZone();
    }

    public void addParticipant() {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.addParticipant();
    }

    public void enterMeetingName() {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.enterMeetingName();
    }

    public void submitInvite() {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.submitInvite();
    }

    public void checkEmailIsReceived() {
        String inviteEmailSubject = checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);
    }

    public String checkEmailContentForNewConfInfo() {
        gmail = new GmailObject(driver2);
        return gmail.checkEmailContentForNewConfInfo();
    }

    public String checkInviteEmail() {
        driver2 = new FirefoxDriver();
        gmail = new GmailObject(driver2);
        return gmail.checkInviteEmail();
    }

    public void closeWindow() {
        driver2.quit();
    }
}
