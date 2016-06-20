package com.lotus.allconferencing.support_classes;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.Utility;
import com.lotus.allconferencing.services.schedulers.components.OldSchedulerComponents;
import com.lotus.allconferencing.services.schedulers.components.SimpleScheduledInviteComponents;
import com.lotus.allconferencing.services.schedulers.pages.OldSchedulerPageObject;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ben on 7/20/2015.
 */

/************
 *
 */
public class GmailObject extends BaseSeleniumTest {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;
    private static WebElement subject;
    private static WebElement emailArrivalTime;
    private static Integer currentHour, currentMinutes, emailMinuteThreshold;
    private static Pattern pattern = null;
    private static Boolean isNewEmail = false;
    private static Boolean isFull = false;
    private static String tollFreeNumArr[];
    private static String passcodeArr[];
    private static String partPasscode = "";


    // Selectors for Gmail Inbox Page----------------------------------------------------------------
//    private static final By EMAIL_SUBJECT = By.xpath("//table[id=':36']/tbody/tr/td:nth-of-type(6)/div/div/div/span"); //-- Works in Firefox
    private static final By EMAIL_SUBJECT = By.cssSelector(".y6 span"); //-- Works in Chrome and Firefox
//    private static final By TIMESTAMP = By.cssSelector("table[id=':36'] tbody tr td:nth-of-type(8) span"); //-- Works in Firefox
    private static final By TIMESTAMP = By.cssSelector(".xW.xY span"); //-- Works in Chrome and Firefox
    private static final By REFRESH = By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div/div/div/div/div/div/div[4]/div");
    private static final By EMAIL_BODY = By.xpath("/html/body/div[7]/div[3]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/table/*");
    //-----------------------------------------------------------------------------------------------


    public GmailObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    GmailLoginPageObject gmail = new GmailLoginPageObject(driver);
    GmailInboxComponentsObject gmailInbox = new GmailInboxComponentsObject(driver);
    OldSchedulerPageObject oldScheduler = new OldSchedulerPageObject(driver);

    public enum MeetingType {
        OLD(0), SIMPLE(1), NEW(2);

        private int meetingType;

        MeetingType(int value) { this.meetingType = value; }

        public int value() { return meetingType; }
    }

    public void get() {

        driver.get("http://www.gmail.com/");

    }


    public String checkInviteEmail() {
        // Login to Gmail
        loginToGmail(gmail);
        System.out.println("Logged in to Gmail!");

        // Inbox - get most recent email, evaluate time received and email subject
        try {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subject = driver.findElement(EMAIL_SUBJECT);
            System.out.println("Email subject found! Subject text is: " + subject.getText());
            WebElement emailArrivalTime = driver.findElement(TIMESTAMP);
            String originalTimestamp = emailArrivalTime.getText();
            System.out.println("Email timestamp found! Timestamp is: " + originalTimestamp);
            String emailTime = waitForEmailTimestamp(originalTimestamp);
            //System.out.println("Email time retrieved from Gmail was: " + emailTime);

            createCheckforNewEmail(emailTime);

            waitForEmailToBeReceived(emailArrivalTime, currentHour, currentMinutes, emailMinuteThreshold);
            System.out.println("Verified email was received!");

        } catch (NoSuchElementException nsee) {
            refreshInbox();
        } catch (ElementNotFoundException enfe) {
            refreshInbox();
        }


        //System.out.println("The subject of the email found is: " + subject.getText());
        return (subject.getText());

    }

    private void loginToGmail(GmailLoginPageObject gmail) {
        gmail = new GmailLoginPageObject(driver);
        gmail.goToGmail();
        Utility.captureScreenshot(driver, "GmailLogin");
        gmail.enterGmailUsername();
        Utility.captureScreenshot(driver, "GmailLoginWithUser");
        gmail.enterGmailPassword();
        Utility.captureScreenshot(driver, "GmailLoginWithPass");
    }

    public void openEmail() {
        subject.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

/*    public WebElement getEmailArrivalTime () {
        gmailInbox = new GmailInboxComponentsObject(driver);
        WebElement emailArrivalTime = gmailInbox.getEmailArrivalTime();
        return emailArrivalTime;
    }*/

    public void refreshInbox () {
        WebElement refreshButton = driver.findElement(REFRESH);
        refreshButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String waitForEmailTimestamp(String emailTime) {
        for (int i = 0; i < 3; i++) {
            if (!emailTime.contains("am")) {
                if (!emailTime.contains("pm")) {
                    WebElement emailArrivalTime = driver.findElement(TIMESTAMP);
                    emailTime = emailArrivalTime.getText();
                }
            }
        }
        return emailTime;
    }

    public void createCheckforNewEmail(String emailTime) {
        String[] emailTimeParts = emailTime.split(":");
        WebDriverWait waitForTableToPopulate = new WebDriverWait(driver, 10);
        waitForTableToPopulate.until(tableIsPopulated(emailTimeParts, 2));
        String emailHourStr = emailTimeParts[0];
        String emailMinuteStr = emailTimeParts[1];
        String[] minuteParts = emailMinuteStr.split("\\s");
        emailMinuteStr = minuteParts[0];

        Integer emailHour = (Integer.parseInt(emailHourStr));
        //System.out.println("Email hour is: " + emailHour);

        Integer emailMinute = (Integer.parseInt(emailMinuteStr));
        //System.out.println("Email minute is: " + emailMinute);

        emailMinuteThreshold = emailMinute + 1;
        //System.out.println("Email Minute Threshold is: " + emailMinuteThreshold);

        DateTime dt = new DateTime();
        //System.out.println("Current DateTime is: " + dt);

        currentHour = getCurrentHour(dt);
        currentMinutes = dt.getMinuteOfHour();
        //System.out.println("Current Hour is: " + currentHour);
        //System.out.println("Current Minute is: " + currentMinutes);
    }

    public Integer getCurrentHour(DateTime dateTime) {
        Integer currentHour = dateTime.getHourOfDay();
        if (currentHour > 12) {
            currentHour -= 12;
        }
        if (currentHour == 0) {
            currentHour = 12;
        }
        return currentHour;
    }

    public void waitForEmailToBeReceived(WebElement emailArrivalTime, Integer currentHour, Integer currentMinutes, Integer emailMinuteThreshold) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElement(emailArrivalTime, String.valueOf(currentHour)));
        Boolean thresholdReached = false;
        for (int i = 0; i < 15; i++) {
            if (currentMinutes <= emailMinuteThreshold) {
                thresholdReached = true;
                i = 15;
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 6 || i == 12) {
                refreshInbox();
            }
        }

        if (thresholdReached == false) {
            //System.out.println("Email time synchronization failed. The time threshold was not reached.");
            System.exit(-1);
        }
    }

    public String checkEmailContentForNewConfInfo(MeetingType meetingType) {
        List<WebElement> emailBodyTable = driver.findElements(EMAIL_BODY);
        if (emailBodyTable.size() == 1) {
//            System.out.println("List was populated upon assignment!");
        } else {
            waitForListToPopulate(emailBodyTable);
        }
        Integer elementCounter = 0;
        String[] emailContent = null;
//        System.out.println(emailBodyTable.size());
        DateTime dateTime = new DateTime();
        Integer mins = dateTime.getMinuteOfHour();
//        System.out.println("Minute of Hour using DateTime: " + mins);
        Integer minThreshold = null;
        if (mins != 0) {
            minThreshold = mins - 1;
        } else {
            minThreshold = 59;
        }
//        System.out.println("Minutes threshold = " + minThreshold);
        String tollFreeLabel_Simple = oldScheduler.EMAIL_TOLL_FREE;
        String tollFreeLabel_Old = oldScheduler.EMAIL_TOLL_FREE;
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
/*                if (line.contains("minute")) {
                    String[] lineFrags = line.split(" ");
                    for (String fragment : lineFrags) {
                        CharArray charArray = fragment.toCharArray();
                        }
                    }
                }*/
                if (line.contains("(0 minutes ago)") || line.contains("(1 minute ago)")) {
//                    System.out.println("Found newest email");
                    isNewEmail = true;
                }
                if (isNewEmail == true) {
                    if (line.contains(tollFreeLabel_Old)) {
                        System.out.println("Toll-free number found!");
                        tollFreeNum = line;
                    } else if (line.contains(tollFreeLabel_Simple)) {
                        System.out.println("Toll free number found!");
                        tollFreeNum = line;
                    } else if (line.contains("Participant Passcode")) {
                        System.out.println("Passcode found!");
                        modPasscode = line;
                    }
                }
//                System.out.println("");
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
                switch (meetingType) {
                    case OLD:
                        System.out.println("This is index (27) in tollFreeNumArr: " + tollFreeNumArr[27]);
                        verifyTollFreeNumberIsGenerated(tollFreeNumArr[27]);
                        System.out.println("This is index (28) in participantPasscode: " + passcodeArr[28]);
                        verifyPasscodesAreGenerated(passcodeArr[28]);
                        partPasscode = passcodeArr[28];
                        break;
                    case SIMPLE:
                        System.out.println("This is index (3) in tollFreeNumArr: " + tollFreeNumArr[3]);
                        verifyTollFreeNumberIsGenerated(tollFreeNumArr[3]);
                        System.out.println("This is index (2) in participantPasscode: " + passcodeArr[2]);
                        verifyPasscodesAreGenerated(passcodeArr[2]);
                        partPasscode = passcodeArr[2];
                        break;
                    default:
                        System.out.println("This is index (27) in tollFreeNumArr: " + tollFreeNumArr[27]);
                        verifyTollFreeNumberIsGenerated(tollFreeNumArr[27]);
                        System.out.println("This is index (28) in participantPasscode: " + passcodeArr[28]);
                        verifyPasscodesAreGenerated(passcodeArr[28]);
                        partPasscode = passcodeArr[28];
                        break;
                }

            } else {
                System.out.println("The new email was not found.");
                System.exit(-1);
            }
        }
        return partPasscode;
    }

    public void waitForListToPopulate(List<WebElement> emailBodyTable) {
        int emailIterateNum = 0;
        while(emailBodyTable.size() != 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            emailIterateNum++;
            if (emailIterateNum==3) {
                break;

            } else {
                System.out.println("Through wait number " + emailIterateNum + ". Table not yet populated.");
            }
        }
        /*
        try {
            Thread.sleep(2000);
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
            System.out.println("Through third wait (6 seconds total) - table was still not populated");
            System.exit(-1);
        }
        */
    }

    public String getOldSchedulerEmailTollFreeLabel() {
        OldSchedulerComponents oldSchedulerComponents = new OldSchedulerComponents(driver);
        return oldSchedulerComponents.getEmailInviteTollFreeLabel();
    }

    public String getSimpleSchedulerEmailTollFreeLabel() {
        SimpleScheduledInviteComponents simpleScheduledInviteComponents = new SimpleScheduledInviteComponents(driver);
        return simpleScheduledInviteComponents.getEmailInviteTollFreeLabel();
    }

    private ExpectedCondition<Boolean> tableIsPopulated(final String[] stringArray, final int expectedSize) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                if (stringArray.length == expectedSize) {
                    isFull = true;
                } else {
                    isFull = false;
                }
                return isFull;
            }
        };
    }

    public void verifyTollFreeNumberIsGenerated(String tollFreeNum) {
        assertTrue("Toll-Free Number has been generated", tollFreeNum.matches("^?[0-9, -]{1,14}"));
    }

    public void verifyPasscodesAreGenerated(String participantPasscode) {
        assertTrue("Passcodes have been generated", participantPasscode.matches("^?[0-9]{1,6}"));
    }
}
