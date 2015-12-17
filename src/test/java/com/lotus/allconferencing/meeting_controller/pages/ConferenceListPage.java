package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.OldAccountServicesComponents;
import com.lotus.allconferencing.meeting_controller.pages.components.V2ConferenceListComponents;
import com.lotus.allconferencing.meeting_controller.pages.components.V2OldSchedulerComponents;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 *
 */
public class ConferenceListPage extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    public ConferenceListPage(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public V2ConferenceListComponents v2ConferenceListComponents = new V2ConferenceListComponents(driver);


    public Boolean getLatestV2ConferencePasscode(String partPasscode) {
        v2ConferenceListComponents = new V2ConferenceListComponents(driver);
        String newConferencePasscode = v2ConferenceListComponents.getLatestV2ConferencePasscode();
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode, conferenceDisplays);
        return conferenceDisplays;
    }

    public Boolean getLatestV1ConferencePasscode(String partPasscode) {
        v2ConferenceListComponents = new V2ConferenceListComponents(driver);
        String newConferencePasscode = v2ConferenceListComponents.getLatestV1ConferencePasscode();
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode, conferenceDisplays);
        return conferenceDisplays;
    }

    public void removeConferenceFromList() {
        v2ConferenceListComponents = new V2ConferenceListComponents(driver);
        WebElement deleteButton = v2ConferenceListComponents.getDeleteButtonForLatestConference();
        deleteButton.click();
        Alert confirmAlert = driver.switchTo().alert();
        confirmAlert.accept();
        WebDriverWait waitForDeletionConfirmationPage = new WebDriverWait(driver, 10);
        waitForDeletionConfirmationPage.until(
                ExpectedConditions.titleIs(v2ConferenceListComponents.getDeletionExpectedTitle())
        );
    }

    public Boolean verifyNewMeetingDisplaysInConferenceList(String expectedPasscode, String actualPasscode, Boolean conferenceDisplays) {
        assertTrue("Passcodes display in conference list", actualPasscode.contains(expectedPasscode));
        conferenceDisplays = true;
        return conferenceDisplays;
    }
}
