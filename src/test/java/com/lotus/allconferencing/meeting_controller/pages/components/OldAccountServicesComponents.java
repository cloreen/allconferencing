package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


/**
 * Created by Ben on 12/6/2015.
 */
public class OldAccountServicesComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Old Account Services Page Components--------------------------------------------
    private static final By SCHEDULE_V2_MEETING = By.cssSelector("a[href='schedule_v2.asp?Rights=0']");
    private static final By SCHEDULE_V1_MEETING = By.cssSelector("a[href='schedule.asp?Rights=0']");
    private static final By LIST_V2_CONFERENCES = By.cssSelector("a[href='edit_conf_v2.asp?Rights=0']");
    private static final By LIST_V1_CONFERENCES = By.cssSelector("a[href='edit_conf.asp?Rights=0']");
    private static final String EXPECTED_TITLE = "All Conferencing - Account Services";
    //-----------------------------------------------------------------------------------------------



    public OldAccountServicesComponents(WebDriver newDriver) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public WebElement getV2ScheduleMeetingLink() {
        WebElement v2ScheduleMeetingLink = driver.findElement(SCHEDULE_V2_MEETING);
        return v2ScheduleMeetingLink;
    }

    public WebElement getV1ScheduleMeetingLink() {
        WebElement v1ScheduleMeetingLink = driver.findElement(SCHEDULE_V1_MEETING);
        return v1ScheduleMeetingLink;
    }

    public WebElement getListV2ConferencesLink() {
        WebElement listV2ConferencesLink = driver.findElement(LIST_V2_CONFERENCES);
        return listV2ConferencesLink;
    }

    public WebElement getListV1ConferencesLink() {
        WebElement listV2ConferencesLink = driver.findElement(LIST_V1_CONFERENCES);
        return listV2ConferencesLink;
    }

    public String getExpectedTitle() {
        return EXPECTED_TITLE;
    }
}
