package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.CorpAccountServicesComponents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 12/17/2015.
 */
public class CorpAccountServicesPage extends PageManager {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    private CorpAccountServicesComponents corpAccountServicesComponents = new CorpAccountServicesComponents(driver);


    // Selectors for Corp Account Services Page Components--------------------------------------------
    private static final By LOGOUT = By.partialLinkText("Logout");
    private static final String CORP_ACCOUNT_SERVICES_TITLE = "Corporate Administrator Home Page";
    //-----------------------------------------------------------------------------------------------


    public CorpAccountServicesPage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        driver.findElement(LOGOUT).click();
        driver.switchTo().alert().accept();
        waitForTitle(driver);
    }
}
