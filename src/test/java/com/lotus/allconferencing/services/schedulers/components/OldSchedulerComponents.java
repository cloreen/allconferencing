package com.lotus.allconferencing.services.schedulers.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


/**
 * Created by Ben on 12/6/2015.
 */
public class OldSchedulerComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Old Scheduler Components--------------------------------------------
    private static final By MEETING_NAME = By.cssSelector("input[name='Conference_Name']");
    private static final By MODERATOR_NAME = By.cssSelector("input[name='Moderator_Name']");
    private static final By SPECIFY_MEETING_TIME_RADIO = By.cssSelector("input[name='Rule_Type'][value='adhoc']");
    private static final By MEETING_HOUR = By.cssSelector("select[name='Rule_Start_Hour']");
    private static final By TIME_OF_DAY = By.cssSelector("select[name='Rule_Start_AM']");
    private static final By TIME_ZONE = By.cssSelector("select[name='cboTimeZone']");
    private static final By PARTICIPANT_EMAIL = By.cssSelector("#txtEmail");
    private static final By ADD_TO_MEETING = By.cssSelector("#cmdAdd");
    private static final By SEND_REMINDER = By.cssSelector("#Checkbox2");
    private static final By SUBMIT = By.cssSelector("input[name='cmdSubmit']");
    private static final By ACCT_SERVICES = By.cssSelector("input[name='Submit']");
    private static final String EXPECTED_TITLE = "All Conferencing - Schedule a Conference";
    private static final String EMAIL_TOLL_FREE = "Toll-free phone number";
    //-----------------------------------------------------------------------------------



    public OldSchedulerComponents (WebDriver newDriver) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public WebElement getMeetingNameField() {
        WebElement meetingNameField = driver.findElement(MEETING_NAME);
        return meetingNameField;
    }

    public WebElement getModeratorNameField() {
        WebElement moderatorNameField = driver.findElement(MODERATOR_NAME);
        return moderatorNameField;
    }

    public WebElement getSpecifyMeetingTimeRadioButton() {
        WebElement specifyMeetingTimeRadioButton = driver.findElement(SPECIFY_MEETING_TIME_RADIO);
        return specifyMeetingTimeRadioButton;
    }

    public List<WebElement> getMeetingHourOptions() {
        WebElement meetingHourElement = driver.findElement(MEETING_HOUR);
        Select meetingHourSelect = new Select(meetingHourElement);
        List<WebElement> meetingHourOptions = meetingHourSelect.getOptions();
        return meetingHourOptions;
    }

    public List<WebElement> getTimeOfDayOptions() {
        WebElement timeOfDayElement = driver.findElement(TIME_OF_DAY);
        Select timeOfDaySelect = new Select(timeOfDayElement);
        List<WebElement> timeOfDayOptions = timeOfDaySelect.getOptions();
        return timeOfDayOptions;
    }

    public List<WebElement> getTimeZoneOptions() {
        WebElement timeZoneElement = driver.findElement(TIME_ZONE);
        Select timeZoneSelect = new Select(timeZoneElement);
        List<WebElement> timeZoneSelectOptions = timeZoneSelect.getOptions();
        return timeZoneSelectOptions;
    }

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

    public String getExpectedTitle() {
        return EXPECTED_TITLE;
    }

    public String getEmailInviteTollFreeLabel() {
        return EMAIL_TOLL_FREE;
    }
}
