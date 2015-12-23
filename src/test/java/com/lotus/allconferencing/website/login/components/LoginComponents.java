package com.lotus.allconferencing.website.login.components;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 12/15/2015.
 */
public class LoginComponents {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;

    // Selectors for Login Components----------------------------------------------------------------
    private static By LOGIN_BUTTON = By.cssSelector("ul[id='MenuBar3']>li>a");
    private static By CLIENT_ID = By.cssSelector("form[id='login']>fieldset>input[type='text']");
    private static By PASSWORD = By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']");
    //-----------------------------------------------------------------------------------------------

    public LoginComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public By getLoginButtonBy() {
        return LOGIN_BUTTON;
    }

    public WebElement getClientIDField() {
        WebElement clientIDField = driver.findElement(CLIENT_ID);
        return clientIDField;
    }

    public WebElement getPasswordField() {
        WebElement passwordField = driver.findElement(PASSWORD);
        return passwordField;
    }


}
