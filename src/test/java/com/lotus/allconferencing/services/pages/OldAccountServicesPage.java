package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.OldAccountServicesComponents;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 * TODO - Comment refactor points
 */
public class OldAccountServicesPage extends PageManager {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;
    private static Integer version = null;

    // Selectors for Old Account Services Page Components--------------------------------------------
    private static final By SCHEDULE_V2_MEETING = By.cssSelector("a[href='schedule_v2.asp?Rights=0']");
    private static final By SCHEDULE_V1_MEETING = By.cssSelector("a[href='schedule.asp?Rights=0']");
    private static final By LIST_V2_CONFERENCES = By.cssSelector("a[href='edit_conf_v2.asp?Rights=0']");
    private static final By LIST_V1_CONFERENCES = By.cssSelector("a[href='edit_conf.asp?Rights=0']");
    //private static final By LOGOUT = By.cssSelector("img[name='logout']");
    private static final By LOGOUT = By.name("logout");
    public static final By TITLE = By.tagName("title");
    private static final String ACCOUNT_SERVICES_EXPECTED_TITLE = "All Conferencing - Account Services";
    private static final String SCHEDULER_EXPECTED_TITLE = "All Conferencing - Schedule a Conference";
    private static final String CONFERENCE_LIST_EXPECTED_TITLE = "List/Edit/Delete Conference";
    //-----------------------------------------------------------------------------------------------


    public OldAccountServicesPage(WebDriver newDriver, Integer inVersion) {
        driver = newDriver;
        version = inVersion;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public OldAccountServicesComponents oldAccountServicesComponents = new OldAccountServicesComponents(driver);
//    public OldSchedulerComponents oldSchedulerComponents = new OldSchedulerComponents(driver);
//    public ConferenceListComponents conferenceListComponents = new ConferenceListComponents(driver);
/*
    public enum Version {
        VERSION1, VERSION2;
    }

    public class ConferenceVersion {
        Version version;

        public ConferenceVersion(Version version) {
            this.version = version;
        }

        public void openOldScheduler(Version version) {
            switch (version){
                case VERSION1:
                    oldAccountServicesComponents = new OldAccountServicesComponents(driver);
                    oldSchedulerComponents = new OldSchedulerComponents(driver);
                    WebElement v1ScheduleMeetingLink = oldAccountServicesComponents.getV1ScheduleMeetingLink();
                    v1ScheduleMeetingLink.click();
                    WebDriverWait waitForV1SchedulerToDisplay = new WebDriverWait(driver, 10);
                    waitForV1SchedulerToDisplay.until(
                            ExpectedConditions.titleIs(oldSchedulerComponents.getExpectedTitle())
                    );
                    break;
                case VERSION2:
                    oldAccountServicesComponents = new OldAccountServicesComponents(driver);
                    oldSchedulerComponents = new OldSchedulerComponents(driver);
                    WebElement v2ScheduleMeetingLink = oldAccountServicesComponents.getV2ScheduleMeetingLink();
                    v2ScheduleMeetingLink.click();
                    WebDriverWait waitForV2SchedulerToDisplay = new WebDriverWait(driver, 10);
                    waitForV2SchedulerToDisplay.until(
                            ExpectedConditions.titleIs(oldSchedulerComponents.getExpectedTitle())
                    );
                    break;
            }
        }
    }
*/
    public OldSchedulerPageObject openV1OldScheduler() {
        driver.findElement(SCHEDULE_V1_MEETING).click();
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(SCHEDULER_EXPECTED_TITLE)
        );
        return new OldSchedulerPageObject(driver);
    }

    public OldSchedulerPageObject openV2OldScheduler() {
        driver.findElement(SCHEDULE_V2_MEETING).click();
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(SCHEDULER_EXPECTED_TITLE)
        );
        return new OldSchedulerPageObject(driver);
    }


/*
    public void openV2OldScheduler() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement v2ScheduleMeetingLink = oldAccountServicesComponents.getV2ScheduleMeetingLink();
        v2ScheduleMeetingLink.click();
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(oldSchedulerComponents.getExpectedTitle())
        );
    }

    public void openV1OldScheduler() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement v1ScheduleMeetingLink = oldAccountServicesComponents.getV1ScheduleMeetingLink();
        v1ScheduleMeetingLink.click();
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(oldSchedulerComponents.getExpectedTitle())
        );
    }
*/
    public void refreshAccountServices(String myAccountWindowHandle) {
//        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        driver.switchTo().window(myAccountWindowHandle);
        driver.navigate().refresh();

        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs(ACCOUNT_SERVICES_EXPECTED_TITLE)
        );
    }

/*   public ConferenceListPage listV1Conferences() {
       driver.findElement(LIST_V1_CONFERENCES).click();
       WebDriverWait waitForConferenceListToDisplay = new WebDriverWait(driver, 10);
       waitForConferenceListToDisplay.until(
               ExpectedConditions.titleIs(CONFERENCE_LIST_EXPECTED_TITLE)
       );
       return new ConferenceListPage(driver);
   }

    public ConferenceListPage listV2Conferences() {
        driver.findElement(LIST_V2_CONFERENCES).click();
        WebDriverWait waitForConferenceListToDisplay = new WebDriverWait(driver, 10);
        waitForConferenceListToDisplay.until(
                ExpectedConditions.titleIs(CONFERENCE_LIST_EXPECTED_TITLE)
        );
        return new ConferenceListPage(driver);
    }*/

    public ConferenceListPage listConferences(Integer version) {
        WebElement conferenceList = null;
        switch(version) {
            case 1:
                conferenceList = driver.findElement(LIST_V1_CONFERENCES);
                break;
            case 2:
                conferenceList = driver.findElement(LIST_V2_CONFERENCES);
                break;
        }
        conferenceList.click();
        WebDriverWait waitForConferenceListToDisplay = new WebDriverWait(driver, 10);
        waitForConferenceListToDisplay.until(
                ExpectedConditions.titleIs(CONFERENCE_LIST_EXPECTED_TITLE)
        );
        return new ConferenceListPage(driver);
    }



/*
    public void listV2Conferences() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        conferenceListComponents = new ConferenceListComponents(driver);
        WebElement listConferencesLink = oldAccountServicesComponents.getListV2ConferencesLink();
        listConferencesLink.click();

        WebDriverWait waitForConferenceList = new WebDriverWait(driver, 10);
        waitForConferenceList.until(
                ExpectedConditions.titleIs(conferenceListComponents.getExpectedTitle())
        );
    }

    public void listV1Conferences() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        conferenceListComponents = new ConferenceListComponents(driver);
        WebElement listConferencesLink = oldAccountServicesComponents.getListV1ConferencesLink();
        listConferencesLink.click();

        WebDriverWait waitForConferenceList = new WebDriverWait(driver, 10);
        waitForConferenceList.until(
                ExpectedConditions.titleIs(conferenceListComponents.getExpectedTitle())
        );
    }
*/
/*
    public void logout() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        WebElement logoutButton = oldAccountServicesComponents.getLogoutButton();
        logoutButton.click();
        driver.switchTo().alert().accept();
        waitForTitle(driver);
    }
*/
    public OldSchedulerPageObject openScheduler(int inVersion) {
        WebDriverWait waitForPageToLoad = new WebDriverWait(driver, 10);
        switch (inVersion) {
            case 1:
                waitForPageToLoad.until(
                        ExpectedConditions.presenceOfElementLocated(SCHEDULE_V1_MEETING)
                ).click();
//                driver.findElement(SCHEDULE_V1_MEETING).click();
                break;
            case 2:
                waitForPageToLoad.until(
                        ExpectedConditions.presenceOfElementLocated(SCHEDULE_V2_MEETING)
                ).click();
//                driver.findElement(SCHEDULE_V2_MEETING).click();
                break;
            default:
                System.out.println("Version not specified. Permitted values are 1 or 2.");
                System.exit(-1);
                break;
        }
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(SCHEDULER_EXPECTED_TITLE)
        );
        return new OldSchedulerPageObject(driver);
    }
}
