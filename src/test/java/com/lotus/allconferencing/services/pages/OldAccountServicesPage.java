package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.ConferenceListComponents;
import com.lotus.allconferencing.services.components.OldAccountServicesComponents;
import com.lotus.allconferencing.services.schedulers.components.OldSchedulerComponents;
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
public class OldAccountServicesPage extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;




    public OldAccountServicesPage(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public OldAccountServicesComponents oldAccountServicesComponents = new OldAccountServicesComponents(driver);
    public OldSchedulerComponents oldSchedulerComponents = new OldSchedulerComponents(driver);
    public ConferenceListComponents conferenceListComponents = new ConferenceListComponents(driver);

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

    public void refreshAccountServices(String myAccountWindowHandle) {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        driver.switchTo().window(myAccountWindowHandle);
        driver.navigate().refresh();

        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs(oldAccountServicesComponents.getExpectedTitle())
        );
    }

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
}
