package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


/**
 * Created by Ben on 12/6/2015.
 */
public class V2OldSchedulerComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Old Scheduler Components--------------------------------------------
    private static final String MEETING_HOUR_BY_CSS = "select[name='Rule_Start_Hour']";
    private static final String TIME_OF_DAY_BY_CSS = "select[name='Rule_Start_AM']";
    private static final By TIME_ZONE = By.cssSelector("select[name='cboTimeZone']");
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

    public String getMeetingHourSelector() {
        String meetingHourSelector = MEETING_HOUR_BY_CSS;
        return meetingHourSelector;
    }

    // CSS Selector for AM/PM will not work in this class. Passing the selector as a String to the Page Object instead.
    public String getTimeOfDaySelector() {
        String timeOfDaySelector = TIME_OF_DAY_BY_CSS;
        return timeOfDaySelector;
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

}
