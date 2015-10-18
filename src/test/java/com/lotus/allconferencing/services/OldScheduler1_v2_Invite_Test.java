package com.lotus.allconferencing.services;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.GmailObject;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 10/13/2015.
 */
public class OldScheduler1_v2_Invite_Test {
    private static WebDriver driver;
    private static WebDriver driver2;
    private LoginPageObject loginPage;
    private GmailObject gmail;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    private static Boolean isNewEmail = false;
    private static Pattern pattern = null;
    private static String newConferencePasscode = "";
    private static String tollFreeNumArr[];
    private static String passcodeArr[];
    private static String partPasscode = "";
    private static String timeOfDay = "";


    @Test
    public void scheduleV2Meeting() {
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

        WebElement v2ScheduleMeetingLink = driver.findElement(By.cssSelector("a[href='schedule_v2.asp?Rights=0']"));
        v2ScheduleMeetingLink.click();

        WebDriverWait waitForSchedulerToDisplay = new WebDriverWait(driver, 10);

        waitForSchedulerToDisplay.until(
                ExpectedConditions.titleIs("All Conferencing - Schedule a Conference")
        );

        // Enter Meeting Name
        WebElement conferenceNameTextBox = driver.findElement(By.cssSelector("input[name='Conference_Name']"));
        conferenceNameTextBox.sendKeys("Test Meeting - Old Scheduler 1");

        // Enter Moderator Name
        WebElement moderatorNameTextBox = driver.findElement(By.cssSelector("input[name='Moderator_Name']"));
        moderatorNameTextBox.sendKeys("Test Moderator");

        // Check specific meeting time (rather than immediate
        WebElement specifyTimeRadioButton = driver.findElement(By.cssSelector("input[name='Rule_Type'][value='adhoc']"));
        specifyTimeRadioButton.click();

        // Choose proper meeting time
        Select meetingHourSelect = new Select(driver.findElement(By.cssSelector("select[name='Rule_Start_Hour']")));
        List<WebElement> meetingHourOptions = meetingHourSelect.getOptions();
        DateTime currentTime = new DateTime();
        Integer currentHour = currentTime.getHourOfDay();
        Integer meetingHour = 0;
        timeOfDay = "";
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

        // Choose AM or PM
        Select timeOfDaySelect = new Select(driver.findElement(By.cssSelector("select[name='Rule_Start_AM']")));
        List<WebElement> timeOfDaySelectOptions = timeOfDaySelect.getOptions();
        int timeOfDaySelectOptionsIteration = 0;
        for (WebElement option : timeOfDaySelectOptions) {
            timeOfDaySelectOptionsIteration++;
            if (timeOfDay == "AM") {
                if (option.getAttribute("value") == "AM") {
                    option.click();
                    break;
                }
            } else if (option.getAttribute("value") == "PM") {
                option.click();
                break;
            }
        }

        // Choose Pacific time zone
        Select timeZoneSelect = new Select(driver.findElement(By.cssSelector("select[name='cboTimeZone']")));
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

        // Add a participant
        WebElement participantEmail = driver.findElement(By.cssSelector("#txtEmail"));
        WebElement addToMeetingButton = driver.findElement(By.cssSelector("#cmdAdd"));
        participantEmail.sendKeys("bgactest03@gmail.com");
        addToMeetingButton.click();

        // Enable Reminder Email
        WebElement sendReminderCheckbox = driver.findElement(By.cssSelector("#Checkbox2"));
        sendReminderCheckbox.click();

        // Submit Form
        WebElement submitButton = driver.findElement(By.cssSelector("input[name='cmdSubmit']"));
        submitButton.click();

        // Go to Account Services
        WebElement acctSvcsButton = driver.findElement(By.cssSelector("input[name='Submit']"));
        acctSvcsButton.click();
        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs("All Conferencing - Account Services")
        );
    }


    @Test
    public void checkEmailAndPasscodes() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check invite email, passcodes and dial-in numbers have been generated
//        getGmailInbox();
        String inviteEmailSubject = checkInviteEmail();
        verifyEmailReceived(inviteEmailSubject);

        WebElement emailSubject = driver2.findElement(By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(6) div div div span"));
        emailSubject.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> emailBodyTable = driver2.findElements(By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/table/*"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through first wait - table not populated");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (emailBodyTable.size() != 1) {
            System.out.println("Through second wait - table still not populated");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (emailBodyTable.size() != 1) {
            System.out.println("Through third wait (15 seconds) - table still not populated");
        }

        Integer elementCounter = 0;
        String[] emailContent = null;
        System.out.println(emailBodyTable.size());
        DateTime dateTime = new DateTime();
        Integer mins = dateTime.getMinuteOfHour();
        System.out.println("Minute of Hour using DateTime: " + mins);
        Integer minThreshold = null;
        if (mins != 0) {
            minThreshold = mins - 1;
        } else {
            minThreshold = 59;
        }
        System.out.println("Minutes threshold = " + minThreshold);
        String tollFreeNum = null;
        String modPasscode = null;
        for (WebElement element : emailBodyTable) {
            if (element.isDisplayed()) {
                emailContent = element.getText().split("\n");
            }
            for (String line : emailContent) {
                elementCounter += 1;
                System.out.println("Line " + elementCounter + ":");
                System.out.println(line);
                if (line.contains("(0 minutes ago)")) {
                    System.out.println("Found newest email");
                    isNewEmail = true;
                }
                if (isNewEmail == true) {
                    if (line.contains("Toll-free phone number")) {
                        System.out.println("Toll-free number found!");
                        tollFreeNum = line;
                    } else if (line.contains("Participant Passcode")) {
                        System.out.println("Passcode found!");
                        modPasscode = line;
                    }
                }
                System.out.println("");
            }

            if (isNewEmail == true) {
                tollFreeNumArr = tollFreeNum.split(" ");
                passcodeArr = modPasscode.split(" ");
                System.out.println("Toll Free Number line:");
                for (String item : tollFreeNumArr) {
                    System.out.println(item);
                }
                System.out.println("");
                System.out.println("Passcode line:");
                for (String item : passcodeArr) {
                    System.out.println(item);
                }
                pattern = Pattern.compile("\\d+");
                System.out.println("This is index (27) in tollFreeNumArr: " + tollFreeNumArr[27]);
                verifyTollFreeNumberIsGenerated(tollFreeNumArr[27]);
                System.out.println("This is index (28) in participantPasscode: " + passcodeArr[28]);
                verifyPasscodesAreGenerated(passcodeArr[28]);
                partPasscode = passcodeArr[28];
            } else {
                System.out.println("The new email was not found.");
                System.exit(-1);
            }
        }
    }

    @Test
    public void checkConferenceList() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver.switchTo().window(myAccountWindow);
        driver.navigate().refresh();

        WebDriverWait waitForAcctSvcs = new WebDriverWait(driver, 10);
        waitForAcctSvcs.until(
                ExpectedConditions.titleIs("All Conferencing - Account Services")
        );

        // Check conference list
        WebElement listConferences = driver.findElement(By.partialLinkText("List/Edit/Delete"));
        listConferences.click();
        WebDriverWait waitForConferenceList = new WebDriverWait(driver, 10);
        waitForConferenceList.until(
                ExpectedConditions.titleIs("List/Edit/Delete Conference")
        );
        WebElement newConferencePasscodeElement = driver.findElement(By.xpath("/html/body/form/table[2]/tbody/tr[3]/td[5]/p"));
        newConferencePasscode = newConferencePasscodeElement.getText();
        verifyNewMeetingDisplaysInConferenceList(newConferencePasscode);
    }

    public void verifyEmailReceived(String emailSubject) {
        assertThat("Appropriate email is received", emailSubject.contentEquals("Your Conference Invitation"));
    }

    public void verifyTollFreeNumberIsGenerated(String tollFreeNum) {
        assertTrue("Toll-Free Number has been generated", tollFreeNum.matches("^?[0-9, -]{1,14}"));
    }

    public void verifyPasscodesAreGenerated(String participantPasscode) {
        assertTrue("Passcodes have been generated", participantPasscode.matches("^?[0-9]{1,6}"));
    }

    public void verifyNewMeetingDisplaysInConferenceList(String participantPasscode) {
        assertTrue("Passcodes display in conference list", participantPasscode.contains(partPasscode));
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
        loginPage.login(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword());
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
        System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public String checkInviteEmail() {
        driver2 = new FirefoxDriver();
        gmail = new GmailObject(driver2);
        driver2.get("http://www.gmail.com/");
        return gmail.checkInviteEmail();
    }
}
