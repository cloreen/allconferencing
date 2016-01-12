package com.lotus.allconferencing.services.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 12/17/2015.
 */
public class CorpAccountServicesComponents {
    private static WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Old Account Services Page Components--------------------------------------------
    private static final By EASY_ALL_INVITE = By.xpath("//table[2]/tbody/tr/td/table/tbody/tr[2]/td/div[3]/div/table/tbody/tr[3]/td/a");
    private static final String EASY_ALL_PAGE_TITLE = "All Conferencing My Services";
    //-----------------------------------------------------------------------------------------------


    public CorpAccountServicesComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebElement getEasyAllInviteLink() {
        WebElement easyAllInviteLink = driver.findElement(EASY_ALL_INVITE);
        return easyAllInviteLink;
    }

    public String getExpectedEasyAllTitle() {
        return EASY_ALL_PAGE_TITLE;
    }

}
