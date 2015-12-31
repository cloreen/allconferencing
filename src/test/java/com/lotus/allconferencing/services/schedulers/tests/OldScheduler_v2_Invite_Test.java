package com.lotus.allconferencing.services.schedulers.tests;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.ConferenceListPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import com.lotus.allconferencing.support_classes.GmailObject;
import com.lotus.allconferencing.webdriver.manager.WindowManager;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 10/13/2015.
 */
/*******************************
 * TODO - Create Type 3 account My Services Page Object
 */

public class OldScheduler_v2_Invite_Test {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private static ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    //private static Boolean isNewEmail = false;
    private static Boolean conferenceDisplays = false;
    /*private static Pattern pattern = null;
    private static String newConferencePasscode = "";
    private static String tollFreeNumArr[];
    private static String passcodeArr[];*/
    private static String partPasscode = "";
    private static String timeOfDay = "";
    private static Integer version = 2; // Version is used to specify whether v1 or v2 scheduler is used.

    WindowManager windowManager = new WindowManager(driver);
    GmailObject gmail = new GmailObject(driver2);
    OldSchedulerPageObject oldScheduler = new  OldSchedulerPageObject(driver);
    OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    ConferenceListPage conferenceListPage = new ConferenceListPage(driver);

    @BeforeClass
    public static void setup() {
        getSettings();
        openBrowser();
    }


    @Test
    public void scheduleMeeting() {
        goToHomePage();
        login(LoginPageObject.LoginType.STANDARD);
        openScheduler();

        // Enter Meeting Info
        setupMeeting(timeOfDay);
        goToAccountServices();
    }

    @Test
    public void checkEmailAndPasscodes() {
        partPasscode = getParticipantPasscodeFromEmail();
        closeEmailWindow();
    }

    @Test
    public void checkConferenceList() {
        // Check conference list
        goToConferenceList();
        conferenceDisplays = checkForNewConference();

        // Remove conference from list once it's found
        if(conferenceDisplays) {
            removeConferenceFromList();
        }
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }


    // Verifications ---------------------------------------------------------------------------------------------------

    public void verifyEmailReceived(String emailSubject) {
        assertThat("Appropriate email is received", emailSubject.contentEquals(readProps.getInviteEmailSubject()));
    }

    // End Verifications -----------------------------------------------------------------------------------------------

    // Helper methods --------------------------------------------------------------------------------------------------

    public static void getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBrowser() {
        driver = new FirefoxDriver();
    }

    public void goToHomePage() {
        driver.get(readProps.getUrl());

        // Click on HTML element -- May be necessary to run tests in Firefox.
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        // Get handle for home page
        baseWindow = getWindow();
    }

    public String getWindow() {
        //Brings current window to foregroud and assignes window handle to a string to easily switch windows later.
        windowManager = new WindowManager(driver);
        String windowHandle = windowManager.getWindow();
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
        switch (version) {
            case 1:
                oldAccountServicesPage.openV1OldScheduler();
                break;
            case 2:
                oldAccountServicesPage.openV2OldScheduler();
                break;
            default:
                System.out.println("Version not specified. Permitted values are 1 or 2.");
                System.exit(-1);
        }
        oldScheduler = new OldSchedulerPageObject(driver);
    }

    private void setupMeeting(String timeOfDay) {
        oldScheduler.enterMeetingName();
        oldScheduler.enterModeratorName();
        oldScheduler.selectSpecifyTime();
        timeOfDay = oldScheduler.selectMeetingHour(timeOfDay);
        oldScheduler.selectTimeOfDay(timeOfDay);
        oldScheduler.choosePacificTimeZone();
        oldScheduler.addParticipant();
        oldScheduler.enableEmailReminders();
        oldScheduler.submitForm();
    }

    public void goToAccountServices() {
        oldScheduler.goToAccountServices();
    }

    // checkEmailIsReceived(), checkInviteEmail(), and checkEmailContentForNewInfo should be condensed into one method.
    public String getParticipantPasscodeFromEmail() {
        driver2 = new FirefoxDriver();
        gmail = new GmailObject(driver2);
        String inviteEmailSubject = gmail.checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);
        gmail.openEmail();
        return gmail.checkEmailContentForNewConfInfo(GmailObject.MeetingType.OLD);
    }

    public void goToConferenceList() {
        oldAccountServicesPage = new OldAccountServicesPage(driver);
        oldAccountServicesPage.refreshAccountServices(myAccountWindow);
        switch (version) {
            case 2:
                oldAccountServicesPage.listV2Conferences();
                break;
            case 1:
                oldAccountServicesPage.listV1Conferences();
                break;
            default:
                System.out.println("Version not specified. Permitted values are 1 or 2.");
                System.exit(-1);
        }
    }

    public Boolean checkForNewConference() {
        conferenceListPage = new ConferenceListPage(driver);
        switch (version) {
            case 2:
                conferenceDisplays = conferenceListPage.getLatestV2ConferencePasscode(partPasscode);
                break;
            case 1:
                conferenceDisplays = conferenceListPage.getLatestV1ConferencePasscode(partPasscode);
                break;
            default:
                System.out.println("Version not specified. Permitted values are 1 or 2.");
                System.exit(-1);
        }
        return conferenceDisplays;
    }

    public void removeConferenceFromList() {
        conferenceListPage.removeConferenceFromList();
    }

    public void closeEmailWindow() {
        driver2.quit();
    }

    // End Helper methods ----------------------------------------------------------------------------------------------
}
