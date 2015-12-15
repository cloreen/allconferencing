package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.OldAccountServicesComponents;
import com.lotus.allconferencing.meeting_controller.pages.components.V2ConferenceListComponents;
import com.lotus.allconferencing.meeting_controller.pages.components.V2OldSchedulerComponents;
import org.joda.time.DateTime;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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
    public V2OldSchedulerComponents v2OldSchedulerComponents = new V2OldSchedulerComponents(driver);
    public V2ConferenceListComponents v2ConferenceListComponents = new V2ConferenceListComponents(driver);

    public void openV2OldScheduler() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        v2OldSchedulerComponents = new V2OldSchedulerComponents(driver);
        WebElement v2ScheduleMeetingLink = oldAccountServicesComponents.getV2ScheduleMeetingLink();
        v2ScheduleMeetingLink.click();
        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);
        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs(v2OldSchedulerComponents.getExpectedTitle())
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

    public void listConferences() {
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        v2ConferenceListComponents = new V2ConferenceListComponents(driver);
        WebElement listConferencesLink = oldAccountServicesComponents.getListConferencesLink();
        listConferencesLink.click();

        WebDriverWait waitForConferenceList = new WebDriverWait(driver, 10);
        waitForConferenceList.until(
                ExpectedConditions.titleIs(v2ConferenceListComponents.getExpectedTitle())
        );
    }
}
