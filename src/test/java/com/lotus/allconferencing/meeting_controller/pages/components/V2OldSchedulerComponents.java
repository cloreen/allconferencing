package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by Ben on 12/6/2015.
 */
public class V2OldSchedulerComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Old Scheduler Components--------------------------------------------
    private static final By PARTICIPANT_EMAIL = By.cssSelector("#txtEmail");
    private static final By ADD_TO_MEETING = By.cssSelector("#cmdAdd");
    private static final By SEND_REMINDER = By.cssSelector("#Checkbox2");
    private static final By SUBMIT = By.cssSelector("input[name='cmdSubmit']");
    private static final By ACCT_SERVICES = By.cssSelector("input[name='Submit']");
    //-----------------------------------------------------------------------------------



    public V2OldSchedulerComponents (WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FindBy(css="input[name='cmdSubmit']")
    protected WebElement submit_button;


    /*
    @FindBy(how = How.CSS, using = "#txtEmail")
    private WebElement participantEmail;
    */

    public WebElement getParticipantEmailField() {
        WebElement participantEmailField = driver.findElement(PARTICIPANT_EMAIL);
        return participantEmailField;
    }

    public WebElement getAddToMeetingButton() {
        WebElement addToMeetingButton = driver.findElement(ADD_TO_MEETING);
        return addToMeetingButton;
    }

    public WebElement getSendReminderCheckbox() {
        WebElement sendReminderCheckbox = driver.findElement(SEND_REMINDER);
        return sendReminderCheckbox;
    }

    public WebElement getSubmitButton() {
        WebElement submitButton = driver.findElement(SUBMIT);
        return submitButton;
    }

    public WebElement getAcctSvcsButton() {
        WebElement acctSvcsButton = driver.findElement(ACCT_SERVICES);
        return acctSvcsButton;
    }

}
