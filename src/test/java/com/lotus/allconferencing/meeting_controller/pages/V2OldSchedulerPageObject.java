package com.lotus.allconferencing.meeting_controller.pages;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.GmailInboxComponentsObject;
import com.lotus.allconferencing.meeting_controller.pages.components.GmailLoginPageObject;
import com.lotus.allconferencing.meeting_controller.pages.components.V2OldSchedulerComponents;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 * TODO - Comment refactor points
 */
public class V2OldSchedulerPageObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;




    public V2OldSchedulerPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public V2OldSchedulerComponents v2OldSchedulerComponents = new V2OldSchedulerComponents(driver);

    public String selectMeetingHour(String timeOfDay) {
        Select meetingHourSelect = new Select(driver.findElement(By.cssSelector(v2OldSchedulerComponents.getMeetingHourSelector())));
        List<WebElement> meetingHourOptions = meetingHourSelect.getOptions();
        //List<WebElement> meetingHourOptions = v2OldSchedulerComponents.getMeetingHourOptions();
        DateTime currentTime = new DateTime();
        Integer currentHour = currentTime.getHourOfDay();
        System.out.println("Current Hour is: " + currentHour);
        Integer meetingHour = 0;
        //timeOfDay = "";
        if (currentHour <= 11 || currentHour == 23) {
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
        //Refactoring By to Components class results in Null Pointer Exception. Refactored the selector as an alternative.
        WebElement timeOfDayElement = driver.findElement(By.cssSelector(v2OldSchedulerComponents.getTimeOfDaySelector()));
        Select timeOfDaySelect = new Select(timeOfDayElement);
        List<WebElement> timeOfDaySelectOptions = timeOfDaySelect.getOptions();

        int timeOfDaySelectOptionsIteration = 0;
        for (WebElement option : timeOfDaySelectOptions) {
            System.out.println("Option " + timeOfDaySelectOptionsIteration + " is: " + option.getAttribute("value"));
            timeOfDaySelectOptionsIteration++;
            if (option.getAttribute("value").contentEquals(timeOfDay)) {
                option.click();
                break;
            }
        }
    }

    public void choosePacificTimeZone() {
        List<WebElement> timeZoneOptions = v2OldSchedulerComponents.getTimeZoneOptions();
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
        WebElement participantEmailField = v2OldSchedulerComponents.getParticipantEmailField();
        WebElement addToMeetingButton = v2OldSchedulerComponents.getAddToMeetingButton();
        participantEmailField.sendKeys(readProps.getParticipantEmail());
        addToMeetingButton.click();
    }

    public void enableEmailReminders() {
        WebElement sendReminderCheckbox = v2OldSchedulerComponents.getSendReminderCheckbox();
        sendReminderCheckbox.click();
    }

    public void submitForm() {
        WebElement submitButton = v2OldSchedulerComponents.getSubmitButton();
        submitButton.click();
    }

    public void goToAccountServices() {
        WebElement acctSvcsButton = v2OldSchedulerComponents.getAcctSvcsButton();
        acctSvcsButton.click();
        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs("All Conferencing - Account Services")
        );
    }

}
