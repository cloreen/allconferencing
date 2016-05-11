package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.schedulers.pages.SimpleScheduledInvitePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 12/17/2015.
 */


public class SimpleAccountServicesPage extends PageManager {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Simple Account Services Page========--------------------------------------------
    private static final By EASY_ALL_INVITE = By.xpath("//table[2]/tbody/tr/td/table/tbody/tr[2]/td/div[3]/div/table/tbody/tr[3]/td/a");
    private static final String SIMPLE_ACCOUNT_SERVICES_TITLE = "Welcome to All Conferencing Services";
    //-----------------------------------------------------------------------------------------------

    public SimpleAccountServicesPage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SimpleScheduledInvitePage openEasyAllInvitePage() {
        driver.findElement(EASY_ALL_INVITE).click();
        waitForTitle(driver);
        return new SimpleScheduledInvitePage(driver);
    }
}
