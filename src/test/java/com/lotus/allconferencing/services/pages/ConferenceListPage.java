package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.*;
import com.lotus.allconferencing.services.components.ConferenceListComponents;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
    private static WebElement newConferencePasscode;

    // Selectors for Old Account Services Page------------------------------------------------------
    private static final String EXPECTED_TITLE = "List/Edit/Delete Conference";
    private static final String DELETION_PAGE_EXPECTED_TITLE = "All Conferencing - Delete Conference";
    private static final By LATEST_V2_CONFERENCE_PASSCODE = By.xpath("/html/body/form/table[2]/tbody/tr[3]/td[5]/p");
    private static final By LATEST_V1_CONFERENCE_PASSCODE = By.xpath("/html/body/form/table[3]/tbody/tr[3]/td[5]/p");
    private static final By LATEST_CONFERENCE_DELETE = By.cssSelector("input[name='cmdRemove']");
    //-----------------------------------------------------------------------------------------------



    public ConferenceListPage(WebDriver newDriver) {

        driver = newDriver;
//        LogEntries logs = driver.manage().logs().get("browser");

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConferenceListComponents conferenceListComponents = new ConferenceListComponents(driver);


/*    public Boolean getLatestV2ConferencePasscode(String partPasscode) {
//        conferenceListComponents = new ConferenceListComponents(driver);
//        String newConferencePasscode = conferenceListComponents.getLatestV2ConferencePasscode();
        WebElement newConferencePasscode = driver.findElement(LATEST_V2_CONFERENCE_PASSCODE);
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode.getText(), conferenceDisplays);
        return conferenceDisplays;
    }

    public Boolean getLatestV1ConferencePasscode(String partPasscode) {
//        conferenceListComponents = new ConferenceListComponents(driver);
//        String newConferencePasscode = conferenceListComponents.getLatestV1ConferencePasscode();
        WebElement newConferencePasscode = driver.findElement(LATEST_V1_CONFERENCE_PASSCODE);
        Boolean conferenceDisplays = false;
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode.getText(), conferenceDisplays);
        return conferenceDisplays;
    }*/

    public Boolean getLatestConferencePasscode(String partPasscode, Integer version) {
        WebElement newConferencePasscode = null;
        Boolean conferenceDisplays = false;
        switch (version) {
            case 1:
                newConferencePasscode = driver.findElement(LATEST_V1_CONFERENCE_PASSCODE);
                break;
            case 2:
                newConferencePasscode = driver.findElement(LATEST_V2_CONFERENCE_PASSCODE);
                break;
        }
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode.getText(), conferenceDisplays);
        System.out.println("New Conference Passcode is: " + newConferencePasscode.getText());
        return conferenceDisplays;
    }

    public Boolean checkForNewConference(String partPasscode, Integer version) {
        WebElement newConferencePasscode = null;
        Boolean conferenceDisplays = false;
        switch (version) {
            case 1:
                newConferencePasscode = driver.findElement(LATEST_V1_CONFERENCE_PASSCODE);
                break;
            case 2:
                newConferencePasscode = driver.findElement(LATEST_V2_CONFERENCE_PASSCODE);
                break;
        }
        conferenceDisplays = verifyNewMeetingDisplaysInConferenceList(partPasscode, newConferencePasscode.getText(), conferenceDisplays);
        System.out.println("New Conference Passcode is: " + newConferencePasscode.getText());
        return conferenceDisplays;
    }

    public void removeConferenceFromList(Integer version) {
//        conferenceListComponents = new ConferenceListComponents(driver);
//        WebElement deleteButton = conferenceListComponents.getDeleteButtonForLatestConference();
        switch(version) {
            case 1:
                newConferencePasscode = driver.findElement(LATEST_V1_CONFERENCE_PASSCODE);
                break;
            case 2:
                newConferencePasscode = driver.findElement(LATEST_V2_CONFERENCE_PASSCODE);
                break;
        }

        String modPass = newConferencePasscode.getText();
        WebElement deleteButton = driver.findElement(LATEST_CONFERENCE_DELETE);
//        Utility.captureScreenshot(driver, "ConferenceList");  // This needs to be commented out if using custom Driver class
        deleteButton.click();
        /*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Utility.captureScreenshot(driver, "Clicked_Delete");
        } catch (UnhandledAlertException uae) {
            // Do nothing. Just continue.
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utility.captureScreenshot(driver, "5_Sec_After_Delete");
        Alert potentialAlert = PageManager.getSeleniumAlert(driver);
        if (potentialAlert != null) {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            System.out.println("Alert was not null.");
        } else {
            System.out.println("Alert was null.");
        }
        */
//        PageManager.getSeleniumAlert(driver, version, modPass);
        if (ExcelData.browser == ExcelData.Browser.valueOf("PHANTOMJS")) {
            PageManager.getSeleniumAlert(driver, version, modPass);
        } else {
            WebDriverWait waitForAlert = new WebDriverWait(driver, 10);
            waitForAlert.until(
                    ExpectedConditions.alertIsPresent()
            );
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }

//        WebDriverWait waitForConfirmAlert = new WebDriverWait(driver, 10);
//        waitForConfirmAlert.until(
//                ExpectedConditions.alertIsPresent()+
//        );
//        Alert confirmAlert = driver.switchTo().alert();
//        confirmAlert.accept();
        WebDriverWait waitForDeletionConfirmationPage = new WebDriverWait(driver, 10);
        waitForDeletionConfirmationPage.until(
//                ExpectedConditions.titleIs(conferenceListComponents.getDeletionExpectedTitle())
                ExpectedConditions.titleIs(DELETION_PAGE_EXPECTED_TITLE)
        );
//        Utility.captureScreenshot(driver, "Post-ConfDeletionPage");   // TakesScreenshot doesn't work with custom Driver class.
        System.out.println("Conference should be deleted!");
    }

    public Boolean verifyNewMeetingDisplaysInConferenceList(String expectedPasscode, String actualPasscode, Boolean conferenceDisplays) {
        assertTrue("Passcodes display in conference list", actualPasscode.contains(expectedPasscode));
        conferenceDisplays = true;
        return conferenceDisplays;
    }

}
