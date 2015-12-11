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
    //-----------------------------------------------------------------------------------



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
}