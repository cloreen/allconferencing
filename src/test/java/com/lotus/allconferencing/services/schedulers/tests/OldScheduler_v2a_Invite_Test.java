package com.lotus.allconferencing.services.schedulers.tests;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ExcelData;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.ConferenceListPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import com.lotus.allconferencing.support_classes.GmailObject;
import com.lotus.allconferencing.website.pages.HomePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.MatcherAssert.assertThat;

/*
* TODO - Handle multiple rows of test data from excel
*
* */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OldScheduler_v2a_Invite_Test extends BaseSeleniumTest {
    private static WebDriver inDriver;
    private static WebDriver driver;
    private static WebDriver driver2;

    private static ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static Boolean conferenceDisplays = false;
    private static String partPasscode = "";
    private static String timeOfDay = "";
    private static Integer version = 2; // Version is used to specify whether v1 or v2 scheduler is used.

    HomePage homePage = new HomePage(driver);
    GmailObject gmail = new GmailObject(driver2);
    OldSchedulerPageObject oldScheduler = new  OldSchedulerPageObject(driver);
    OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    ConferenceListPage conferenceListPage = new ConferenceListPage(driver);
    static ExcelData excelData = new ExcelData();

    @BeforeClass
    public static void setup() {
        readProps = getSettings();
        ExcelData.getDataFromExcel();
        driver = openBrowser(ExcelData.browser);
    }

    @Test
    public void scheduleMeetingAndCheckPasscodes() {
        //Login and schedule meeting
//        homePage.login(AccountType.LoginType.STANDARD, AccountType.AcctType.STANDARD_OLD, LoginPageObject.AccessType.LOGIN);
        homePage.login(ExcelData.loginType, ExcelData.accountType, ExcelData.accessType);
        oldAccountServicesPage.openScheduler(version);
        oldScheduler.setupMeeting(timeOfDay, version);

        //Get passcode from email
        driver2 = openBrowser(ExcelData.browser);
        partPasscode = getParticipantPasscodeFromEmail();
        closeEmailWindow();

        //Check passcode against conference listed in account
        baseWindow = homePage.baseWindow;
        oldAccountServicesPage.refreshAccountServices(baseWindow);
        oldAccountServicesPage.listConferences(version);
        conferenceDisplays = conferenceListPage.checkForNewConference(partPasscode, version);
        assertThat("Assert conference displays in account", conferenceDisplays);
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

    public static WebDriver openBrowser(ExcelData.Browser inBrowser) {
        String browser = inBrowser.toString();
        inDriver = setDriver(BrowserName.valueOf(browser));
        return inDriver;
    }

    // checkEmailIsReceived(), checkInviteEmail(), and checkEmailContentForNewInfo should be condensed into one method.
    public String getParticipantPasscodeFromEmail() {
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

