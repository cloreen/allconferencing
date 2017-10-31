package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 1/21/2016.
 */
public class NewAccountServicesPage {
    private WebDriver driver;
    private ReadPropertyFile readProps = null;

    public NewAccountServicesPage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
