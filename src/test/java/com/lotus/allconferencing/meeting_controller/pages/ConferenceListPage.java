package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.ConferenceListComponents;
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

    public ConferenceListComponents conferenceListComponents = new ConferenceListComponents(driver);


    public Boolean getLatestV2ConferencePasscode(String partPasscode) {
        conferenceListComponents = new ConferenceListComponents(driver);
        String newConferencePasscode = conferenceListComponents.getLatestV2ConferencePasscode();
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode, conferenceDisplays);
        return conferenceDisplays;
    }

    public Boolean getLatestV1ConferencePasscode(String partPasscode) {
        conferenceListComponents = new ConferenceListComponents(driver);
        String newConferencePasscode = conferenceListComponents.getLatestV1ConferencePasscode();
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode, conferenceDisplays);
        return conferenceDisplays;
    }

    public void removeConferenceFromList() {
        conferenceListComponents = new ConferenceListComponents(driver);
        WebElement deleteButton = conferenceListComponents.getDeleteButtonForLatestConference();
        deleteButton.click();
        Alert confirmAlert = driver.switchTo().alert();
        confirmAlert.accept();
        WebDriverWait waitForDeletionConfirmationPage = new WebDriverWait(driver, 10);
        waitForDeletionConfirmationPage.until(
                ExpectedConditions.titleIs(conferenceListComponents.getDeletionExpectedTitle())
        );
    }

    public Boolean verifyNewMeetingDisplaysInConferenceList(String expectedPasscode, String actualPasscode, Boolean conferenceDisplays) {
        assertTrue("Passcodes display in conference list", actualPasscode.contains(expectedPasscode));
        conferenceDisplays = true;
        return conferenceDisplays;
    }
}
