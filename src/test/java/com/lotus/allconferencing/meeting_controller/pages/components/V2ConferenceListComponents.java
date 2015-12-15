package com.lotus.allconferencing.meeting_controller.pages.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 12/14/2015.
 */
public class V2ConferenceListComponents {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;

    // Selectors for Old Account Services Page Components--------------------------------------------
    //private static final String EXPECTED_TITLE =
    //-----------------------------------------------------------------------------------------------


    public V2ConferenceListComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshConferenceList() {

    }
}
