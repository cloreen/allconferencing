package com.lotus.allconferencing.services.schedulers.tests;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.ConferenceListPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import com.lotus.allconferencing.support_classes.GmailObject;
import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import com.lotus.allconferencing.website.pages.HomePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 10/13/2015.
 */
/*******************************
 * TODO - Create Type 3 account My Services Page Object
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OldScheduler_v2_Invite_Test extends BaseSeleniumTest {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private LoginPageObject.AccessType accessType;
    private static ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static Boolean conferenceDisplays = false;
    private static String partPasscode = "";
    private static String timeOfDay = "";
    private static Integer version = 2; // Version is used to specify whether v1 or v2 scheduler is used.

    HomePage homePage = new HomePage(driver);
    GmailObject gmail = new GmailObject(driver2);
    OldSchedulerPageObject oldScheduler = new  OldSchedulerPageObject(driver);
    OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    ConferenceListPage conferenceListPage = new ConferenceListPage(driver);

    public enum Browser {FIREFOX, CHROME, IE, OPERA, SAUCELABS, HTMLUNIT}

    @BeforeClass
    public static void setup() {
        readProps = getSettings();
        openBrowser(Browser.FIREFOX);
    }


    @Test
    public void test01_scheduleMeeting() {
        homePage.login(AccountType.LoginType.STANDARD, AccountType.AcctType.STANDARD_OLD, LoginPageObject.AccessType.LOGIN);
        oldAccountServicesPage.openScheduler(version);

        // Enter Meeting Info
        oldScheduler.setupMeeting(timeOfDay, version);
    }

    @Test
    public void test02_checkEmailAndPasscodes() {
        partPasscode = getParticipantPasscodeFromEmail();
        closeEmailWindow();
    }

    @Test
    public void test03_checkConferenceList() {
        // Check conference list
        baseWindow = homePage.baseWindow;
        oldAccountServicesPage.refreshAccountServices(baseWindow);
//        goToConferenceList();
        oldAccountServicesPage.listConferences(version);
        conferenceDisplays = conferenceListPage.checkForNewConference(partPasscode, version);

        // Remove conference from list once it's found
        if(conferenceDisplays) {
            conferenceListPage.removeConferenceFromList();
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

    public static void openBrowser(Browser inBrowser) {
        String browser = inBrowser.toString();
//        driver = new FirefoxDriver();
        driver = setDriver(BrowserName.valueOf(browser));
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

    public void closeEmailWindow() {
        driver2.quit();
    }

    // End Helper methods ----------------------------------------------------------------------------------------------
}
