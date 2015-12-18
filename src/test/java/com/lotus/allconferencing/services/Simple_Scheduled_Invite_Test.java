package com.lotus.allconferencing.services;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.meeting_controller.pages.OldSchedulerPageObject;
import com.lotus.allconferencing.meeting_controller.pages.SimpleAccountServicesPage;
import com.lotus.allconferencing.meeting_controller.pages.SimpleScheduledInvitePage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Set;

/**
 * Created by Ben on 12/17/2015.
 */
public class Simple_Scheduled_Invite_Test {
    private static WebDriver driver;
    private LoginPageObject loginPageObject;
    private ReadPropertyFile readProps = null;
    private static String baseWindow;
    private static String myAccountWindow;
    public static String timeOfDay = "";

    public Simple_Scheduled_Invite_Test() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SimpleScheduledInvitePage simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);

    @Test
    public void sendEasyAllInvite() {
        driver = new FirefoxDriver();
        driver.get(readProps.getUrl());

        SimpleAccountServicesPage simpleAccountServicesPage = new SimpleAccountServicesPage(driver);

        login(LoginPageObject.LoginType.STANDARD);
        simpleAccountServicesPage.openEasyAllInvitePage();
        timeOfDay = selectMeetingHour(timeOfDay);
        selectTimeOfDay(timeOfDay);
        selectTimeZone();
    }


    public static String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        //List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public void login(LoginPageObject.LoginType loginType) {
        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        //System.out.println("Base window handle is: " + baseWindow);

        loginPageObject = new LoginPageObject(driver);
        loginPageObject.selectLogin(loginType);
        myAccountWindow = getWindow();
        loginPageObject.login(readProps.getOldAcctClientID(), readProps.getOldAcctPassword());

        //System.out.println("My Account window handle is: " + myAccountWindow);
    }

    public String selectMeetingHour(String timeOfDay) {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        timeOfDay = simpleScheduledInvitePage.selectMeetingHour(timeOfDay);
        return timeOfDay;
    }

    public void selectTimeOfDay(String timeOfDay) {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.selectTimeOfDay(timeOfDay);
    }

    public void selectTimeZone() {
        simpleScheduledInvitePage = new SimpleScheduledInvitePage(driver);
        simpleScheduledInvitePage.choosePacificTimeZone();
    }
}
