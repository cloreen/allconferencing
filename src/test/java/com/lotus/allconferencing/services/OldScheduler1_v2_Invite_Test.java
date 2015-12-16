package com.lotus.allconferencing.services;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 10/13/2015.
 */
/*******************************
 * TODO - Create Type 3 account My Services Page Object
 */

public class OldScheduler1_v2_Invite_Test {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static Boolean isNewEmail = false;
    private static Boolean conferenceDisplays = false;
    private static Pattern pattern = null;
    private static String newConferencePasscode = "";
    private static String tollFreeNumArr[];
    private static String passcodeArr[];
    private static String partPasscode = "";
    private static String timeOfDay = "";

    GmailObject gmail = new GmailObject(driver2);
    public V2OldSchedulerPageObject v2OldScheduler = new V2OldSchedulerPageObject(driver);
    public OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    public V2ConferenceListPage v2ConferenceListPage = new V2ConferenceListPage(driver);

    @Test
    public void scheduleV2Meeting() {

        getSettings();

        openBrowser();
        goToHomePage();
        login(LoginPageObject.LoginType.STANDARD);
        openScheduler();

        // Enter Meeting Info
        enterMeetingName();
        enterModeratorName();
        selectSpecifyTime(); // Check Radio Button to schedule for a specific time
        timeOfDay = selectMeetingHour(timeOfDay); // Get hour of the day and select meeting hour
        selectTimeOfDay(timeOfDay); // Choose AM or PM based on current hour
        selectTimeZone();
        addParticipant();
        enableEmailReminders();
        submitForm();

        goToAccountServices();
    }


    @Test
    public void checkEmailAndPasscodes() {
        getSettings();

        // Check invite email, passcodes and dial-in numbers have been generated
        checkEmailIsReceived();
        gmail.openEmail();
        partPasscode = checkEmailContentForNewConfInfo();
        closeWindow();
    }

    @Test
    public void checkConferenceList() {
        getSettings();
        refreshAccountServices();

        // Check conference list
        goToV2ConferenceList();

        conferenceDisplays = checkForNewConference();

        // Cleanup after test by deleting conference from list
        if(conferenceDisplays) {
            removeConferenceFromList();
        }
    }


    // Verifications ---------------------------------------------------------------------------------------------------

    public void verifyEmailReceived(String emailSubject) {
        assertThat("Appropriate email is received", emailSubject.contentEquals(readProps.getInviteEmailSubject()));
    }

    // End Verifications -----------------------------------------------------------------------------------------------

    // Helper methods --------------------------------------------------------------------------------------------------

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

        loginPage = new LoginPageObject(driver);
        loginPage.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPage.login(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword());

        //System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public void openScheduler() {
        oldAccountServicesPage = new OldAccountServicesPage(driver);
        oldAccountServicesPage.openV2OldScheduler();
    }

    public void enterMeetingName() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.enterMeetingName();
    }

    public void enterModeratorName() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.enterModeratorName();
    }

    public void selectSpecifyTime() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.selectSpecifyTime();
    }

    public String selectMeetingHour(String timeOfDay) {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        timeOfDay = v2OldScheduler.selectMeetingHour(timeOfDay);
        return timeOfDay;
    }

    public void selectTimeOfDay(String timeOfDay) {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.selectTimeOfDay(timeOfDay);
    }

    public void selectTimeZone() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.choosePacificTimeZone();
    }

    public void addParticipant() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.addParticipant();
    }

    public void enableEmailReminders() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.enableEmailReminders();
    }

    public void submitForm() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.submitForm();
    }

    public void goToAccountServices() {
        v2OldScheduler = new V2OldSchedulerPageObject(driver);
        v2OldScheduler.goToAccountServices();
    }

    public String checkInviteEmail() {
        driver2 = new FirefoxDriver();
        gmail = new GmailObject(driver2);
        return gmail.checkInviteEmail();
    }

    public void checkEmailIsReceived() {
        String inviteEmailSubject = checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);
    }

    public String checkEmailContentForNewConfInfo() {
        gmail = new GmailObject(driver2);
        return gmail.checkEmailContentForNewConfInfo();
    }

    public void refreshAccountServices() {
        oldAccountServicesPage = new OldAccountServicesPage(driver);
        oldAccountServicesPage.refreshAccountServices(myAccountWindow);
    }

    public void goToV2ConferenceList() {
        oldAccountServicesPage = new OldAccountServicesPage(driver);
        oldAccountServicesPage.listConferences();
    }

    public Boolean checkForNewConference() {
        v2ConferenceListPage = new V2ConferenceListPage(driver);
        conferenceDisplays = v2ConferenceListPage.getLatestConferencePasscode(partPasscode);
        return conferenceDisplays;
    }

    public void removeConferenceFromList() {
        v2ConferenceListPage = new V2ConferenceListPage(driver);
        v2ConferenceListPage.removeConferenceFromList();
    }

    public void closeWindow() {
        driver2.close();
    }

    // End Helper methods ----------------------------------------------------------------------------------------------
}
