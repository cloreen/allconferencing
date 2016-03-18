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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OldScheduler_v1a_Invite_Test extends BaseSeleniumTest {
    private static WebDriver inDriver;
    private static WebDriver driver;
    private static WebDriver driver2;
    private static Collection<Object[]> excelData;
    private static ArrayList<Object[]> dataArray;
    private static Object[] excelObjectArray;
    private static Object excelObject;
    private static List<Object> dataList;
    private LoginPageObject loginPage;
//    private LoginPageObject.AccessType accessType;
    private static ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static Boolean conferenceDisplays = false;
    private static String partPasscode = "";
    private static String timeOfDay = "";
    private static Integer version = 1; // Version is used to specify whether v1 or v2 scheduler is used.

    HomePage homePage = new HomePage(driver);
    GmailObject gmail = new GmailObject(driver2);
    OldSchedulerPageObject oldScheduler = new  OldSchedulerPageObject(driver);
    OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    ConferenceListPage conferenceListPage = new ConferenceListPage(driver);

    static AccountType.LoginType loginType;
    static AccountType.AcctType accountType;
    static LoginPageObject.AccessType accessType;
    static Browser browser;

    public enum Browser {FIREFOX, CHROME, IE, OPERA, SAUCELABS, HTMLUNIT}

    @BeforeClass
    public static void setup() {
        readProps = getSettings();
        driver = openBrowser(Browser.FIREFOX);
        getDataFromExcel();
    }

    @Test
    public void scheduleMeetingAndCheckPasscodes() {
        //Login and schedule meeting
//        homePage.login(AccountType.LoginType.STANDARD, AccountType.AcctType.STANDARD_OLD, LoginPageObject.AccessType.LOGIN);
        homePage.login(loginType, accountType, accessType);
        oldAccountServicesPage.openScheduler(version);
        oldScheduler.setupMeeting(timeOfDay, version);

        //Get passcode from email
        driver2 = openBrowser(Browser.FIREFOX);
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

    public static WebDriver openBrowser(Browser inBrowser) {
        String browser = inBrowser.toString();
        inDriver = setDriver(BrowserName.valueOf(browser));
        return inDriver;
    }

    public static void getDataFromExcel() {
        try {
            excelData = dataFromExcel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataArray = (ArrayList<Object[]>)excelData;
        for(Object[] objectArray : dataArray) {
            evaluateExcelData(objectArray);
        }
    }

    private static void evaluateExcelData(Object[] inObjArray) {
        int i = 0;
        for(Object object : inObjArray) {
            System.out.println("Object " + object + " in objectArray from Excel sheet is: " + object.toString());
            switch(i) {
                case 0:
                    switch(object.toString()) {
                        case "FIREFOX":
                            browser = Browser.FIREFOX;
                            break;
                        case "CHROME":
                            browser = Browser.CHROME;
                            break;
                        case "IE":
                            browser = Browser.IE;
                            break;
                        case "OPERA":
                            browser = Browser.OPERA;
                            break;
                        case "HTMLUNIT":
                            browser = Browser.HTMLUNIT;
                            break;
                    }
                case 1:
                    switch(object.toString()) {
                        case "STANDARD":
                            loginType = AccountType.LoginType.STANDARD;
                            break;
                        case "CORPORATE":
                            loginType = AccountType.LoginType.CORPORATE;
                            break;
                        case "PARTICIPANT":
                            loginType = AccountType.LoginType.PARTICIPANT;
                            break;
                    }
                    break;
                case 2:
                    switch(object.toString()) {
                        case "STANDARD_OLD":
                            accountType = AccountType.AcctType.STANDARD_OLD;
                            break;
                        case "STANDARD_NEW":
                            accountType = AccountType.AcctType.STANDARD_NEW;
                            break;
                        case "STANDARD_SIMPLE":
                            accountType = AccountType.AcctType.STANDARD_SIMPLE;
                            break;
                    }
                    break;
                case 3:
                    switch (object.toString()) {
                        case "LOGIN":
                            accessType = LoginPageObject.AccessType.LOGIN;
                    }
                    break;
                case 4:
                    LoginPageObject.clientID = object.toString();
                    break;
                case 5:
                    LoginPageObject.clientPass = object.toString();
                    break;
            }
            i++;
        }
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

