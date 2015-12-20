package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Created by Ben on 12/17/2015.
 */
public class SimpleScheduledInviteComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Simple Scheduled Invite Page Components--------------------------------------------
    private static final By MEETING_HOUR = By.cssSelector("#ddnConfHour");
    private static final By TIME_OF_DAY = By.cssSelector("#ddnConfMer");
    private static final By TIME_ZONE = By.cssSelector("#ddnConfZone");
    private static final By ADD_PARTICIPANTS = By.cssSelector("#txtAddrs");
    private static final By MEETING_NAME = By.cssSelector("#txtSubject");
    //-----------------------------------------------------------------------------------------------

    public SimpleScheduledInviteComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<WebElement> timeZoneOptions = timeZoneSelect.getOptions();
        return timeZoneOptions;
    }

    public WebElement getAddParticipantsField() {
        WebElement addParticipantsField = driver.findElement(ADD_PARTICIPANTS);
        return addParticipantsField;
    }

    public WebElement getMeetingNameField() {
        WebElement meetingNameField = driver.findElement(MEETING_NAME);
        return meetingNameField;
    }

}
