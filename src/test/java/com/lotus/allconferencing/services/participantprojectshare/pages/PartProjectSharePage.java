package com.lotus.allconferencing.services.participantprojectshare.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 1/14/2016.
 */
public class PartProjectSharePage extends PageManager {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;

    // Selectors for Participant Project Share Components----------------------------------------------------------------
    private static By CLOSE_SESSION = By.id("yui-gen38");
    private static String EXPECTED_TITLE = "Participant Web";
    //-----------------------------------------------------------------------------------------------



    public PartProjectSharePage(WebDriver newDriver) {
        driver = newDriver;
        try {
            readProps = new ReadPropertyFile();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        WebDriverWait waitForLogoutToBeClickable = new WebDriverWait(driver, 10);
        waitForLogoutToBeClickable.until(
                ExpectedConditions.elementToBeClickable(driver.findElement(CLOSE_SESSION))
        );
        driver.findElement(CLOSE_SESSION).click();
        fluentlyWaitForTitleToDisappear();
    }

    private FluentWait fluentlyWaitForTitleToDisappear() {
        FluentWait fluentlyWaitForTitleToDisappear = new FluentWait(driver);
        fluentlyWaitForTitleToDisappear
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .withTimeout(15, TimeUnit.SECONDS)
                .until(
                        ExpectedConditions.not(participantWebDisplays())
                );
        return fluentlyWaitForTitleToDisappear;
    }

    private ExpectedCondition<Boolean> participantWebDisplays() {
        ExpectedCondition<Boolean> participantWebDisplays = null;
        participantWebDisplays = ExpectedConditions.titleIs(EXPECTED_TITLE);
        return participantWebDisplays;
    }

}
