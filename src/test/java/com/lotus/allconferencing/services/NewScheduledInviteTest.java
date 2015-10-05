package com.lotus.allconferencing.services;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.AddDocPageObject;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.suites.MeetingControllerSuiteTest;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.chrono.Chronology;
import java.util.*;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 9/1/2015.
 */
public class NewScheduledInviteTest extends BaseSeleniumTest {
    private static WebDriver driver; // = getDriver();
    private LoginPageObject loginPage;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static String meetingManagerWindow;


    public WebDriver getBrowser(String browserType) {
        if(driver == null) {
            if(browserType.equals("firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("chrome")) {
            }
        }
        return driver;
    }

    public static String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public void getLoginPage(LoginPageObject.LoginType loginType) {
        System.out.println("Base window handle is: " + baseWindow);

        loginPage = new LoginPageObject(driver);
        loginPage.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPage.login(readProps.getOwnerClientID(), readProps.getOwnerPassword());
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
        System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public void startNextMonth (Integer month, Integer day) {
        month += 1;
        day = 1;
    }

    public void startNextDay (Integer day) {
        day += 1;
    }


    @Test
    public void scheduleInMeetingManager() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver = new FirefoxDriver();
        driver.get(readProps.getUrl());

        // Click on HTML element -- May be necessary to run tests in Firefox.
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        // Get handle for home page
        baseWindow = driver.getWindowHandle();

        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        getLoginPage(LoginPageObject.LoginType.STANDARD);

        // Open Meeting Manager
        WebElement meetingMgrLink = driver.findElement(By.xpath(".//*[@id='lnkMM']"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        meetingMgrLink.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Transfer driver to new window, bring Meeting Manager window to the foreground,
        // get its handle.
        meetingManagerWindow = getWindow();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.manage().window().maximize();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait waitForMeetingManager = new WebDriverWait(driver, 10);
        waitForMeetingManager.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='zb__App__Calendar_title']"))
        );


        // Open calendar
        WebElement calendarTab = driver.findElement(By.xpath(".//*[@id='zb__App__Calendar_title']"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        calendarTab.click();

        // Change Calendar view to current day and wait for display
        WebElement calDayViewButton = driver.findElement(By.xpath(".//*[@id='zb__CLD__DAY_VIEW_title']"));
        calDayViewButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open New Appointment Interface
        WebElement newAppointmentButton = driver.findElement(By.xpath(".//*[@id='zb__NEW_MENU_title']"));
        newAppointmentButton.click();

        // Enter Appointment Name (Subject) --------------------------------------------
        WebElement appointmentLabel = driver.findElement(By.xpath(".//*[@id='zb__App__tab_APPT-1']/table"));
        System.out.println("Text found in element: " + appointmentLabel.getText());
        WebElement saveAndCloseButton = driver.findElement(By.id("zb__APPT-1__SAVE_title"));
        System.out.println("Text found in Save Button: " + saveAndCloseButton.getText());
        WebElement attendeesButton = driver.findElement(By.xpath(".//*[@id='APPT_COMPOSE_1']/div/div/table/tbody/tr/td/table/tbody/tr[5]/td/div/table/tbody/tr/td[2]"));
        System.out.println("Text found in Attendees Button: " + attendeesButton.getText());
        WebElement fromLabel = driver.findElement(By.xpath(".//*[@id='APPT_COMPOSE_1']/div/div/table/tbody/tr/td/table/tbody/tr[3]/td[1]"));
        System.out.println("Text found in From Label: " + fromLabel.getText());
        WebElement subjectField = driver.findElement(By.xpath(".//*[@id='APPT_COMPOSE_1']/div/div/table/tbody/tr/td/table/tbody/tr[4]/td[2]/div/input"));
        subjectField.sendKeys("Test Meeting");

        // Add an Attendee --------------------------------------------------------------
        WebElement attendeesField = driver.findElement(By.id("APPT_COMPOSE_1_person_input"));
        attendeesField.sendKeys(new String("bgactest03@gmail.com"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        attendeesField.sendKeys(Keys.ENTER);

        // Add Start Time ------------------------------------------------------------
        // First, get current time and convert to 12hr format, also get day of week.
        DateTime currentTime = new DateTime();
        Integer currentHour = currentTime.getHourOfDay();
        Integer currentMinutes = currentTime.getMinuteOfHour();
        Integer dayOfWeek = currentTime.getDayOfWeek();
        String dayOfWeekStr = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeekStr = "Monday";
                break;
            case 2:
                dayOfWeekStr = "Tuesday";
                break;
            case 3:
                dayOfWeekStr = "Wednesday";
                break;
            case 4:
                dayOfWeekStr = "Thursday";
                break;
            case 5:
                dayOfWeekStr = "Friday";
                break;
            case 6:
                dayOfWeekStr = "Saturday";
                break;
            case 7:
                dayOfWeekStr = "Sunday";
                break;
        }
        System.out.println("Today is: " + dayOfWeekStr);

        // Now figure appointment time, convert to 12hr format, and calculate AM/PM
        Integer apptHour = currentHour + 1;
        Boolean apptAtMidnight = false;
        if (apptHour == 24) {
            dayOfWeek += 1;
            apptAtMidnight = true;
        }
        Integer apptMinutes;
        if (currentMinutes < 58) {
            if (currentMinutes != 0) {
                apptMinutes = 0;
            } else {
                apptMinutes = 15;
            }
        } else {
            apptMinutes = 15;
        }
        String apptTimeofDay = "";
        if (apptHour >= 12) {
            apptTimeofDay = "PM";
        } else {
            apptTimeofDay = "AM";
        }

        if (apptHour >= 13)
            apptHour -= 12;



        // Input the appointment time in the Start Time field
        String apptStartTime = "";
        WebElement startTimeField = driver.findElement(By.id("ZmTimeInputSelect_1_startTimeInput"));
        startTimeField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        if (apptMinutes == 0) {
            if(apptAtMidnight)
                apptTimeofDay = "AM";
            apptStartTime = apptHour.toString() + ":" + apptMinutes.toString() + "0" + " " + apptTimeofDay;
        } else {
            if(apptAtMidnight)
                apptTimeofDay = "AM";
            apptStartTime = apptHour + ":" + apptMinutes + " " + apptTimeofDay;
        }
        startTimeField.sendKeys(apptStartTime);

        // If appointment is at Midnight, make it the following day.
        if (apptAtMidnight) {
            WebElement startDateElement = driver.findElement(By.xpath(".//*[@id='APPT_COMPOSE_1']/div/div/table/tbody/tr/td/table/tbody/tr[10]/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/input"));
            String startDate = startDateElement.getAttribute("value").toString();
            String[] startDateParts = startDate.split("/");
            System.out.println("StartDate is: " + startDate);
            for (int i = 0; i < startDateParts.length; i++) {
                System.out.println("StartDatePart " + i + " - " + startDateParts[i]);
            }
            String startDateMonth = startDateParts[0];
            String startDateDay = startDateParts[1];
            String startDateYear = startDateParts[2];
            Integer startDateMnthInt = Integer.parseInt(startDateMonth);
            Integer startDateDayInt = Integer.parseInt(startDateDay);
            Integer startDateYrInt = Integer.parseInt(startDateYear);

            switch (startDateMnthInt) {
                case 1:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 2:
                    if (startDateDayInt == 28) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 3:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 4:
                    if (startDateDayInt == 30) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 5:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 6:
                    if (startDateDayInt == 30) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 7:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 8:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 9:
                    if (startDateDayInt == 30) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 10:
                    if (startDateDayInt == 31) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 11:
                    if (startDateDayInt == 30) {
                        startNextMonth(startDateMnthInt, startDateDayInt);
                        ;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
                case 12:
                    if (startDateDayInt == 31) {
                        startDateMnthInt = 1;
                        startDateDayInt = 1;
                        startDateYrInt += 1;
                    } else {
                        startNextDay(startDateDayInt);
                    }
                    break;
            }
            startDateMonth = startDateMnthInt.toString();
            startDateDay = startDateDayInt.toString();
            startDateYear = startDateYrInt.toString();
            startDateElement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            startDateElement.sendKeys(new String(startDateMonth + "/" + startDateDay + "/" + startDateYear));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Save Appointment and Send Appointment Invite
        WebElement sendInviteButton = driver.findElement(By.xpath(".//*[@id='zb__APPT-1__SEND_INVITE_title']"));
        sendInviteButton.click();

        WebDriverWait waitForCalendar = new WebDriverWait(driver, 10);
        waitForCalendar.until(
                ExpectedConditions.presenceOfElementLocated(By.id("zb__CLD__DAY_VIEW_title"))
        );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
        if(apptAtMidnight) {
            WebElement pageForwardButton = driver.findElement(By.xpath(".//td[contains('CAL_Nav_PAGE_FORWARD')]"));
            pageForwardButton.click();
            waitForCalendar.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("zb__CLD__DAY_VIEW_title"))
            );
        }
        */
        // Find Appointment in Calendar to add Meeting Services
        Boolean testApptExists = false;
        Integer elementListIteration = 0;
        WebElement testAppt = null;
        List<WebElement> apptList = driver.findElements(By.xpath(".//td[@class='appt_time']/table/tbody/tr/td"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        for (WebElement appt : apptList) {
            elementListIteration += 1;
            //System.out.println("This is iteration " + elementListIteration);

            String apptText = appt.getText();

            //System.out.println("AppointmentStartTime is: " + apptStartTime);
            //System.out.println("Time of appt is: " + apptText);
            if (apptText.equals(apptStartTime)) {
                testApptExists = true;
                testAppt = appt;
                break;
                //System.out.println("This is the test appt you created");
            } else {
                //System.out.println("This is not the test appt you created");
            }
        }



        assert (testApptExists);

        // Add Meeting Services-----------------------------------------------------------------
        // Open Meeting Services for Test Appointment
        Actions actions = new Actions(driver);
        actions.contextClick(testAppt).perform();
        actions.click(driver.findElement(By.xpath(".//*[@id='AllConferencing_Services_Zimlet_ActionMenu_title']"))).perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Add Moderator Name
        /*
        List<WebElement> mtgSvcsElementList = driver.findElements(By.xpath("./html/body/div[@id='z_shell']/*"));
        Integer elementCounter = 0;
        System.out.println(mtgSvcsElementList.size());
        for (WebElement element : mtgSvcsElementList) {
            elementCounter += 1;
            System.out.println("Element number: " + elementCounter);
            System.out.println(element.getAttribute("innerHTML"));
            System.out.println(element.getText());
            if(element.isDisplayed()) {
                System.out.println("This element is visible");
            }
            System.out.println("");
        }
        */
        /*
        List<WebElement> mtgSvcsElementList = driver.findElements(By.xpath("./html/body/div[@id='z_shell']/div[60]/div[@class='DwtDialog WindowOuterContainer']/*"));
        Integer elementCounter = 0;
        System.out.println(mtgSvcsElementList.size());
        for (WebElement element : mtgSvcsElementList) {
            elementCounter += 1;
            System.out.println("Element number: " + elementCounter);
            System.out.println(element.getAttribute("innerHTML"));
            System.out.println(element.getText());
            if(element.isDisplayed()) {
                System.out.println("This element is visible");
            }
            System.out.println("");
        }
        */
        WebElement mtgSvcsTable = driver.findElement(By.xpath("./html/body/div[@id='z_shell']/div[60]/div[@class='DwtDialog WindowOuterContainer']/table"));
        List<WebElement> mtgSvcsElementList = mtgSvcsTable.findElements(By.tagName("td"));
        Integer elementCounter = 0;
        System.out.println(mtgSvcsElementList.size());
        for (WebElement element : mtgSvcsElementList) {
            elementCounter += 1;
            /*
            System.out.println("Element number: " + elementCounter);
            System.out.println(element.getAttribute("innerHTML"));
            System.out.println(element.getText());
            if(element.isDisplayed()) {
                System.out.println("This element is visible");
            }
            System.out.println("");
            */
            if(elementCounter == 232) {
                WebElement modName = driver.findElement(By.xpath(".//td[contains(.,'Moderator Name*:')]/following-sibling::td[1]/div/input"));
                modName.sendKeys("Moderator 1");

                Actions clickOK = new Actions(driver);
                clickOK.clickAndHold(element).release().perform();
            }
        }

        /*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click OK Button to save as Meeting
        //WebElement meetingOptionsTab = driver.findElement(By.xpath(".//td[contains(text(), 'Meeting Options')]"));
        //meetingOptionsTab.click();
        //WebElement modNameLabel = driver.findElement(By.xpath(".//td[contains(.,'Moderator Name*:')]"));
        boolean elementWorks = false;
        WebElement okFirstDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]"));
        WebElement okSecondDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]/div"));
        WebElement okThirdDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]/div/table"));
        WebElement okFourthDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]/div/table/tbody/tr/td"));
        WebElement okFifthDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]/div/table/tbody/tr/td[2]"));
        WebElement okSixthDirectElement = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]/div/table/tbody/tr/td[3]"));
        WebElement[] okElementArray = {okFirstDirectElement, okSecondDirectElement, okThirdDirectElement, okFourthDirectElement, okFifthDirectElement, okSixthDirectElement};
        WebElement buttonContainer = driver.findElement(By.xpath(".//td[@class='WindowInnerContainer]/div[2]"));
        Actions actions4 = new Actions(driver);
        System.out.println("Element Interaction Attempt 1: Using Actions to click on elements\n");
        for (WebElement element : okElementArray) {
            try {
                actions4.click(element).perform();
            } catch (MoveTargetOutOfBoundsException mtoobe) {
                System.out.println("Exception occurred on " + element);
                continue;
            }

            try {
//                modName.click();
            } catch (ElementNotVisibleException enve) {
                elementWorks = true;
            } catch (NoSuchElementException nsee) {
                elementWorks = true;
            }
            if (elementWorks == true) {
                System.out.println("This element works = " + element.getAttribute("innerHTML"));
                break;
            } else {
                System.out.println("Element didn't work.");
            }

        }

        if(elementWorks == false) {
            System.out.println("Element Interaction Attempt 2: Using Actions to move to lower part of inner container\n");
            try {
                actions4.moveToElement(buttonContainer);
                System.out.println("Moving to container worked!");
            } catch (MoveTargetOutOfBoundsException mtoobe) {
                System.out.println("Target Out of Bounds exception occurred.");
            }
        }





        Actions builder = new Actions(driver);
        //builder.moveToElement(modNameLabel).moveByOffset(606, 355).clickAndHold().release().build().perform();
        //builder.moveToElement(modNameLabel).moveByOffset(344, 348).clickAndHold().release().build().perform();
        //WebDriverWait waitToBeClickable = new WebDriverWait(driver, 10);
        //waitToBeClickable.until(ExpectedConditions.elementToBeClickable(cancelButton));
        //modNameLabel.click();

        //builder.moveToElement(cancelButton).perform();


        WebElement meetingAgendaLabel = driver.findElement(By.xpath(".//*[contains(text(), 'Meeting Agenda')]"));
        meetingAgendaLabel.click();
        WebElement okButtonContainer = driver.findElement(By.xpath(".//td[contains(@id, 'OK_')]"));
        WebElement okButton = driver.findElement(By.xpath(".//td[contains(@id, 'button2_title')]"/div/table/tbody/tr/td[2]"));
        String scrollPageDown = "scroll(0, 300)";
        ((JavascriptExecutor) driver).executeScript(scrollPageDown);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Actions actions1 = new Actions(driver);
        actions1.moveToElement(okButtonContainer)./*moveToElement(driver.findElement(By.xpath(".//td[contains(@id, 'button2_title')]"))).build().perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        builder.clickAndHold().release().perform();

//        actions1.clickAndHold(okButton).release();

        //Actions actions1 = new Actions(driver);
        //actions1.clickAndHold(okButton).build().perform();
        //System.out.println(okButton.getAttribute("innerHTML"));
        //String js = "arguments[0].style.visibility='visible'; arguments[0].mousedown(); arguments[0].mouseup()";
        //((JavascriptExecutor) driver).executeScript(js, okButton);
        //okButton.click();
        //actions1.release(okButton).perform();
        */

        // Click Refresh Button to refresh Calendar
        WebElement refreshButton = driver.findElement(By.xpath(".//td[@id='skin_spacing_global_buttons']"));
        Actions actions2 = new Actions(driver);
        actions2.clickAndHold(refreshButton).release().perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that passcodes and dial-in numbers have been generated
        Integer elementListIterationAfterMtgSvcs = 0;
        WebElement testApptAfterMtgSvcs = null;
        List<WebElement> apptListAfterMtgSvcs = driver.findElements(By.xpath(".//td[@class='appt_time']/table/tbody/tr/td"));
        for (WebElement appt : apptListAfterMtgSvcs) {
            elementListIterationAfterMtgSvcs += 1;
            //System.out.println("This is iteration " + elementListIteration);

            String apptText = appt.getText();

            //System.out.println("AppointmentStartTime is: " + apptStartTime);
            //System.out.println("Time of appt is: " + apptText);
            if (apptText.equals(apptStartTime)) {
                testApptExists = true;
                testApptAfterMtgSvcs = appt;
                break;
                //System.out.println("This is the test appt you created");
            } else {
                //System.out.println("This is not the test appt you created");
            }
        }

        Actions finalActions = new Actions(driver);
        finalActions.contextClick(testApptAfterMtgSvcs).perform();
        finalActions.click(driver.findElement(By.xpath(".//*[@id='AllConferencing_Services_Zimlet_ActionMenu_title']"))).perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement mtgSvcsLeftPanel = driver.findElement(By.xpath("./html/body/div[@id='z_shell']/div[33]/div[10]/div/div/div[2]/div[2]/div/table"));
        WebElement mtgSvcsTag = mtgSvcsLeftPanel.findElement(By.xpath(".//td[contains(@id, '_textCell')]"));
        mtgSvcsTag.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        WebElement mtgSvcsTableAfterMtgSvcs = driver.findElement(By.xpath("./html/body/div[@id='z_shell']/div[60]/div[@class='DwtDialog WindowOuterContainer']/table/tbody/tr[2]/td/div/div/div[2]/div/table[2]/tbody/tr[11]/td[2]/div/table"));
        List<WebElement> mtgSvcsElementListAfterMtgSvcs = mtgSvcsTableAfterMtgSvcs.findElements(By.tagName("td"));
        Integer elementCounterAfterMtgSvcs = 0;
        System.out.println(mtgSvcsElementListAfterMtgSvcs.size());
        for (WebElement element : mtgSvcsElementListAfterMtgSvcs) {
            elementCounterAfterMtgSvcs += 1;

            System.out.println("Element number: " + elementCounterAfterMtgSvcs);
            System.out.println(element.getAttribute("innerHTML"));
            System.out.println(element.getText());
            if(element.isDisplayed()) {
                System.out.println("This element is visible");
            }
            System.out.println("");
            if (elementCounterAfterMtgSvcs == 2) {
                System.out.println(element.getAttribute("value"));
            }
            /*
            if(elementCounter == 232) {
                WebElement modName = driver.findElement(By.xpath(".//td[contains(.,'Moderator Name*:')]/following-sibling::td[1]/div/input"));
                modName.sendKeys("Moderator 1");

                Actions clickOK = new Actions(driver);
                clickOK.clickAndHold(element).release().perform();
            }
            */
        }
    }
}
