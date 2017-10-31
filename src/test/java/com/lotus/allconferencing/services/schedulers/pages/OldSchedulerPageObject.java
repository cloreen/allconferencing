package com.lotus.allconferencing.services.schedulers.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 * TODO - Comment refactor points
 */
public class OldSchedulerPageObject extends PageManager {
//    private static Driver driver;
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;
    private static Integer version;



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
    private static final String ACCT_SERVICES_PAGE = "https://www.allconferencing.com/pages-conference-calls/account_services.asp?Rights=0";
    private static final String EXPECTED_TITLE = "All Conferencing - Schedule a Conference";
    public static final String EMAIL_TOLL_FREE = "Toll-free phone number";
    //-----------------------------------------------------------------------------------


    public OldSchedulerPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public OldSchedulerComponents oldSchedulerComponents = new OldSchedulerComponents(driver);
//    public OldAccountServicesComponents oldAccountServicesComponents = new OldAccountServicesComponents(driver);

    public void enterMeetingName(Integer version) {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement conferenceNameTextBox = driver.findElement(MEETING_NAME);
        conferenceNameTextBox.sendKeys(new String(readProps.getScheduledConfName(version)));
    }

    public void enterModeratorName() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement moderatorNameTextBox = driver.findElement(MODERATOR_NAME);
        moderatorNameTextBox.sendKeys(new String(readProps.getModeratorName()));
    }

    public void selectSpecifyTime() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement specifyTimeRadioButton = driver.findElement(SPECIFY_MEETING_TIME_RADIO);
        specifyTimeRadioButton.click();
    }

    public String selectMeetingHour(String timeOfDay) {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement meetingHourElement = driver.findElement(MEETING_HOUR);
        Select meetingHourSelect = new Select(meetingHourElement);
        List<WebElement> meetingHourOptions = meetingHourSelect.getOptions();
        DateTime currentTime = new DateTime();
        Integer currentHour = currentTime.getHourOfDay();
        //System.out.println("Current Hour is: " + currentHour);
        Integer meetingHour = 0;
        if (currentHour <= 10 || currentHour == 23) {
            timeOfDay = "A.M.";
        } else {
            timeOfDay = "P.M.";
        }
        if (currentHour > 12) {
            currentHour -= 12;
        }
        if (currentHour == 12) {
            meetingHour = 1;
        } else {
            meetingHour = currentHour + 1;
        }
        int selectOptionsIteration = 0;
        for (WebElement option : meetingHourOptions) {
            selectOptionsIteration++;
            Integer optionValue = Integer.parseInt(option.getAttribute("value"));
            if (optionValue == meetingHour) {
                option.click();
                break;
            } else if (selectOptionsIteration == meetingHourOptions.size()) {
                System.out.println("System never found correct option.");
                break;
            } else {
                continue;
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Time Of Day is: " + timeOfDay);
        return timeOfDay;
    }

    public void selectTimeOfDay(String timeOfDay) {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        System.out.println("Time Of Day is: " + timeOfDay);
        WebElement timeOfDayElement = driver.findElement(TIME_OF_DAY);
        Select timeOfDaySelect = new Select(timeOfDayElement);
        List<WebElement> timeOfDaySelectOptions = timeOfDaySelect.getOptions();
        int timeOfDaySelectOptionsIteration = 0;
        for (WebElement option : timeOfDaySelectOptions) {
            //System.out.println("Option " + timeOfDaySelectOptionsIteration + " is: " + option.getAttribute("value"));
            timeOfDaySelectOptionsIteration++;
            if (option.getAttribute("value").contentEquals(timeOfDay)) {
                option.click();
                break;
            }
        }
    }

    public void choosePacificTimeZone() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement timeZoneElement = driver.findElement(TIME_ZONE);
        Select timeZoneSelect = new Select(timeZoneElement);
        List<WebElement> timeZoneOptions = timeZoneSelect.getOptions();
        int timeZoneOptionsIteration = 0;
        for (WebElement option : timeZoneOptions) {
            timeZoneOptionsIteration++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (timeZoneOptionsIteration == 4) { // Option evaluation as String does not work here, so click on option in 4th iteration of loop.
                option.click();
                break;
            } else if (timeZoneOptionsIteration == timeZoneOptions.size()) {
                System.out.println("System never found correct option.");
                break;
            } else {
                continue;
            }
        }
    }

    public void addParticipant() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement participantEmailField = driver.findElement(PARTICIPANT_EMAIL);
        WebElement addToMeetingButton = driver.findElement(ADD_TO_MEETING);
        participantEmailField.sendKeys(readProps.getParticipantEmail());
        addToMeetingButton.click();
    }

    public void enableEmailReminders() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
//        WebElement sendReminderCheckbox = driver.findElement(SEND_REMINDER);
        driver.findElement(SEND_REMINDER).click();
    }

    public void submitForm() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
//        WebElement submitButton = driver.findElement(SUBMIT);
        driver.findElement(SUBMIT).click();
        waitForTitleFluently(driver);
    }

    public OldAccountServicesPage goToAccountServices() {
//        oldSchedulerComponents = new OldSchedulerComponents(driver);
//        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
//        WebElement acctSvcsButton = oldSchedulerComponents.getAcctSvcsButton();
        /*driver.findElement(ACCT_SERVICES).click();
        waitForTitle(driver);*/
        driver.get(ACCT_SERVICES_PAGE); // HtmlUnitDriver is too fast for navigating through UI here, so we go straight to the page URL.
        return new OldAccountServicesPage(driver, version);
    }

    public void setupMeeting(String timeOfDay, Integer version) {
        enterMeetingName(version);
        enterModeratorName();
        selectSpecifyTime();
        timeOfDay = selectMeetingHour(timeOfDay);
        selectTimeOfDay(timeOfDay);
        choosePacificTimeZone();
        addParticipant();
        enableEmailReminders();
//        Utility.captureScreenshot(driver, "FilledScheduler");     // TakesScreenshot doesn't work with custom Driver class
        submitForm();
        goToAccountServices();
    }
}
