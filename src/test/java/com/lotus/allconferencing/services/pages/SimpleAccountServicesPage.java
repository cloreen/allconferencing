package com.lotus.allconferencing.services.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.SimpleAccountServicesComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 12/17/2015.
 */
public class SimpleAccountServicesPage {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    public SimpleAccountServicesPage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openEasyAllInvitePage() {
        SimpleAccountServicesComponents simpleAccountServicesComponents = new SimpleAccountServicesComponents(driver);
        WebElement easyAllInvite = simpleAccountServicesComponents.getEasyAllInviteLink();
        easyAllInvite.click();
        WebDriverWait waitForInvitePage = new WebDriverWait(driver, 10);
        waitForInvitePage.until(
                ExpectedConditions.titleIs(simpleAccountServicesComponents.getExpectedEasyAllTitle())
        );
    }
}
