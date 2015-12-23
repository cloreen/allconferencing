package com.lotus.allconferencing.services.schedulers.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.OldAccountServicesComponents;
import com.lotus.allconferencing.services.schedulers.components.OldSchedulerComponents;
import org.joda.time.DateTime;
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
public class OldSchedulerPageObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;




    public OldSchedulerPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public OldSchedulerComponents oldSchedulerComponents = new OldSchedulerComponents(driver);
    public OldAccountServicesComponents oldAccountServicesComponents = new OldAccountServicesComponents(driver);

    public void enterMeetingName() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement conferenceNameTextBox = oldSchedulerComponents.getMeetingNameField();
        conferenceNameTextBox.sendKeys(new String(readProps.getv2ScheduledConfName()));
    }

    public void enterModeratorName() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement moderatorNameTextBox = oldSchedulerComponents.getModeratorNameField();
        moderatorNameTextBox.sendKeys(new String(readProps.getModeratorName()));
    }

    public void selectSpecifyTime() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement specifyTimeRadioButton = oldSchedulerComponents.getSpecifyMeetingTimeRadioButton();
        specifyTimeRadioButton.click();
    }

    public String selectMeetingHour(String timeOfDay) {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        List<WebElement> meetingHourOptions = oldSchedulerComponents.getMeetingHourOptions();
        DateTime currentTime = new DateTime();
        Integer currentHour = currentTime.getHourOfDay();
        //System.out.println("Current Hour is: " + currentHour);
        Integer meetingHour = 0;
        if (currentHour <= 10 || currentHour == 23) {
            timeOfDay = "AM";
        } else {
            timeOfDay = "PM";
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

        return timeOfDay;
    }

    public void selectTimeOfDay(String timeOfDay) {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        List<WebElement> timeOfDaySelectOptions = oldSchedulerComponents.getTimeOfDayOptions();
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
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        List<WebElement> timeZoneOptions = oldSchedulerComponents.getTimeZoneOptions();
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
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement participantEmailField = oldSchedulerComponents.getParticipantEmailField();
        WebElement addToMeetingButton = oldSchedulerComponents.getAddToMeetingButton();
        participantEmailField.sendKeys(readProps.getParticipantEmail());
        addToMeetingButton.click();
    }

    public void enableEmailReminders() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement sendReminderCheckbox = oldSchedulerComponents.getSendReminderCheckbox();
        sendReminderCheckbox.click();
    }

    public void submitForm() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        WebElement submitButton = oldSchedulerComponents.getSubmitButton();
        submitButton.click();
    }

    public void goToAccountServices() {
        oldSchedulerComponents = new OldSchedulerComponents(driver);
        oldAccountServicesComponents = new OldAccountServicesComponents(driver);
        WebElement acctSvcsButton = oldSchedulerComponents.getAcctSvcsButton();
        acctSvcsButton.click();
        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs(oldAccountServicesComponents.getExpectedTitle())
        );
    }

}
