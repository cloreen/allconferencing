package com.lotus.allconferencing.services.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 12/14/2015.
 */
public class ConferenceListComponents {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;

    // Selectors for Old Account Services Page Components--------------------------------------------
    private static final String EXPECTED_TITLE = "List/Edit/Delete Conference";
    private static final String DELETION_PAGE_EXPECTED_TITLE = "All Conferencing - Delete Conference";
    private static final By LATEST_V2_CONFERENCE_PASSCODE = By.xpath("/html/body/form/table[2]/tbody/tr[3]/td[5]/p");
    private static final By LATEST_V1_CONFERENCE_PASSCODE = By.xpath("/html/body/form/table[3]/tbody/tr[3]/td[5]/p");
    private static final By LATEST_CONFERENCE_DELETE = By.cssSelector("input[name='cmdRemove']");
    //-----------------------------------------------------------------------------------------------


    public ConferenceListComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLatestV2ConferencePasscode() {
        WebElement latestV2ConferencePasscodeElement = driver.findElement(LATEST_V2_CONFERENCE_PASSCODE);
        return latestV2ConferencePasscodeElement.getText();
    }

    public String getLatestV1ConferencePasscode() {
        WebElement latestV1ConferencePasscodeElement = driver.findElement(LATEST_V1_CONFERENCE_PASSCODE);
        return latestV1ConferencePasscodeElement.getText();
    }

    public WebElement getDeleteButtonForLatestConference() {
        WebElement deleteButton = driver.findElement(LATEST_CONFERENCE_DELETE);
        return deleteButton;
    }

    public String getDeletionExpectedTitle() {
        return DELETION_PAGE_EXPECTED_TITLE;
    }

    public String getExpectedTitle() {
        return EXPECTED_TITLE;
    }
}
