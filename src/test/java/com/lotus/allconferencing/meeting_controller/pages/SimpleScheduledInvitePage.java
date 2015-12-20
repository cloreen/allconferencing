package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.OldSchedulerComponents;
import com.lotus.allconferencing.meeting_controller.pages.components.SimpleScheduledInviteComponents;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Ben on 12/17/2015.
 */
public class SimpleScheduledInvitePage {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    public SimpleScheduledInvitePage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SimpleScheduledInviteComponents simpleScheduledInviteComponents;

    public String selectMeetingHour(String timeOfDay) {
        simpleScheduledInviteComponents = new SimpleScheduledInviteComponents(driver);
        List<WebElement> meetingHourOptions = simpleScheduledInviteComponents.getMeetingHourOptions();
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
        simpleScheduledInviteComponents = new SimpleScheduledInviteComponents(driver);
        List<WebElement> timeOfDaySelectOptions = simpleScheduledInviteComponents.getTimeOfDayOptions();
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
        simpleScheduledInviteComponents = new SimpleScheduledInviteComponents(driver);
        List<WebElement> timeZoneOptions = simpleScheduledInviteComponents.getTimeZoneOptions();
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
        simpleScheduledInviteComponents = new SimpleScheduledInviteComponents(driver);
        WebElement addParticipant = simpleScheduledInviteComponents.getAddParticipantsField();
        addParticipant.sendKeys(readProps.getParticipantEmail());
    }
}
