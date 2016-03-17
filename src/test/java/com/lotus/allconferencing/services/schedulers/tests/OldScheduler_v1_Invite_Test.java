package com.lotus.allconferencing.services.schedulers.tests;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.ConferenceListPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import com.lotus.allconferencing.support_classes.GmailObject;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 10/13/2015.
 */

/*******************************
 * TODO - Create Type 3 account My Services Page Object
 * TODO - Rename this class/file, V2ConferenceListComponents, V2OldSchedulerComponents
 * TODO - Create copy of class for v1 test *
 */

public class OldScheduler_v1_Invite_Test extends BaseSeleniumTest {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private ReadPropertyFile readProps = null;
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
    private static Integer version = 1; // Version defines whether v1 or v2 scheduler is used

    private GmailObject gmail = new GmailObject(driver2);
    private OldSchedulerPageObject oldScheduler = new  OldSchedulerPageObject(driver);
    private OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    private ConferenceListPage conferenceListPage = new ConferenceListPage(driver);



    @Test
    public void scheduleMeeting() {

        getSettings();

        openBrowser();
        goToHomePage();
//        login(LoginPageObject.LoginType.STANDARD);
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
        readProps = getSettings();
        refreshAccountServices();

        // Check conference list
//        goToConferenceList();

        conferenceDisplays = checkForNewConference();

        // Cleanup after test by deleting conference from list
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

/*    public void getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void openBrowser() {
        driver = BaseSeleniumTest.setDriver(BrowserName.FIREFOX);
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
/*
    public void login(LoginPageObject.LoginType loginType) {
        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        //System.out.println("Base window handle is: " + baseWindow);

//        loginPage = new LoginPageObject(driver);
        loginPage.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPage.login(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword());

        //System.out.println("My Account window handle is: " + myAccountWindow);
    }
*/
    public void openScheduler() {
//        oldAccountServicesPage = new OldAccountServicesPage(driver);
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

    }

    public void enterMeetingName() {
        oldScheduler = new OldSchedulerPageObject(driver);
//        oldScheduler.enterMeetingName();
    }

    public void enterModeratorName() {
        oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.enterModeratorName();
    }

    public void selectSpecifyTime() {
        oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.selectSpecifyTime();
    }

    public String selectMeetingHour(String timeOfDay) {
 //       oldScheduler = new OldSchedulerPageObject(driver);
        timeOfDay = oldScheduler.selectMeetingHour(timeOfDay);
        return timeOfDay;

    }

    public void selectTimeOfDay(String timeOfDay) {
   //     oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.selectTimeOfDay(timeOfDay);
    }

    public void selectTimeZone() {
     //   oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.choosePacificTimeZone();
    }

    public void addParticipant() {
       // oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.addParticipant();
    }

    public void enableEmailReminders() {
//        oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.enableEmailReminders();
    }

    public void submitForm() {
//        oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.submitForm();
    }

    public void goToAccountServices() {
//        oldScheduler = new OldSchedulerPageObject(driver);
        oldScheduler.goToAccountServices();
    }

    public String checkInviteEmail() {
        driver2 = BaseSeleniumTest.setDriver(BrowserName.FIREFOX);
        gmail = new GmailObject(driver2);
        return gmail.checkInviteEmail();
    }

    public void checkEmailIsReceived() {
        String inviteEmailSubject = checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);
    }

    public String checkEmailContentForNewConfInfo() {
        gmail = new GmailObject(driver2);
        return gmail.checkEmailContentForNewConfInfo(GmailObject.MeetingType.OLD);
    }

    public void refreshAccountServices() {
//        oldAccountServicesPage = new OldAccountServicesPage(driver);
        oldAccountServicesPage.refreshAccountServices(myAccountWindow);
    }

/*    public void goToConferenceList() {
//        oldAccountServicesPage = new OldAccountServicesPage(driver);
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
    }*/

    public Boolean checkForNewConference() {
//        conferenceListPage = new ConferenceListPage(driver);
/*        switch (version) {
            case 2:
                conferenceDisplays = conferenceListPage.getLatestV2ConferencePasscode(partPasscode);
                break;
            case 1:
                conferenceDisplays = conferenceListPage.getLatestV1ConferencePasscode(partPasscode);
                break;
            default:
                System.out.println("Version not specified. Permitted values are 1 or 2.");
                System.exit(-1);
        }*/
        return conferenceDisplays;
    }

    public void removeConferenceFromList() {
 //       conferenceListPage = new ConferenceListPage(driver);
        conferenceListPage.removeConferenceFromList();
    }

    public void closeWindow() {
        driver2.close();
    }

    // End Helper methods ----------------------------------------------------------------------------------------------
}
