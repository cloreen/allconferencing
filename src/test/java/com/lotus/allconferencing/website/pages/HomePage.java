package com.lotus.allconferencing.website.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 1/14/2016.
 */
public class HomePage extends PageManager {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;
    public static String baseWindow, loginWindow;


    // Selectors for Home Page -----------------------------------------------------------------------------------------
    public static final By TITLE = By.tagName("title");
    public static final String EXPECTED_TITLE = "All Conferencing | Affordable Phone Audio, Video, & Web Conferencing for Business";
    private static By ACCT_BUTTON = By.cssSelector("ul[id='MenuBar3']>li>a");
    //------------------------------------------------------------------------------------------------------------------

    public HomePage(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(driver.getTitle() != EXPECTED_TITLE) {
            driver.get(readProps.getUrl());
            baseWindow = driver.getWindowHandle();
        }
    }

    public LoginPageObject login(AccountType.LoginType loginType, AccountType.AcctType acctType, LoginPageObject.AccessType accessType) {
        WebElement acctButton = getElementWithIndex(ACCT_BUTTON, loginType.value());
        acctButton.click();

        // ***DO NOT DELETE - Opens new page in a new window (contextClick() + sendKeys("w") = open in new window)
        /*
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(ACCT_BUTTON, loginType.value())).perform();
        actions.sendKeys(new String("w")).perform();
        */

        // Switch driver to new window, and assign window handle to string for easy reference later
        loginWindow = getNewWindow(driver, driver.getWindowHandles());

        // Wait for new page to display, then return new page
        waitForTitle(driver);
        return new LoginPageObject(driver, loginType, acctType, accessType);
    }

    private WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }
}
