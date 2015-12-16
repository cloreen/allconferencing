package com.lotus.allconferencing.meeting_controller.pages.components;

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

    // Selectors for Login Components--------------------------------------------
        public static By LOGIN_BUTTON = By.cssSelector("ul[id='MenuBar3']>li>a");
    //-----------------------------------------------------------------------------------------------

    public LoginComponents(WebDriver newDriver) {
        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public WebElement getLoginButton() {
        WebElement loginButton = driver.findElement(LOGIN_BUTTON);
        return loginButton;
    }

    public By getLoginButtonBy() {
        return LOGIN_BUTTON;
    }



}
