package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.meeting_controller.pages.components.LoginComponents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 7/20/2015.
 */

/*******
 * TODO - Refactor selectors out to Home Page and Login Components Classes
 */

public class LoginPageObject {
    private WebDriver driver;
    private LoginType accountType;
    private ReadPropertyFile readProps = null;

    private WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public enum LoginType {
        STANDARD(0), CORP(1), PART(2);

        private int loginIndex;

        LoginType(int value) { this.loginIndex = value; }

        public int value() { return loginIndex; }
    }

    public LoginPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get() {

        driver.get(readProps.getUrl());

    }


    public void selectLogin(LoginType loginType) {
        LoginComponents loginComponents = new LoginComponents(driver);
        accountType = loginType;
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(loginComponents.getLoginButtonBy(), loginType.value())).perform();
        actions.sendKeys(new String("w")).perform();// Opens new page in a new window (contextClick() + sendKeys("w") = open in new window)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ExpectedCondition<Boolean> loginPageDisplays(LoginType loginType) {
        ExpectedCondition<Boolean> expectedCondition = null;

        switch(loginType) {
            case STANDARD:
                expectedCondition = ExpectedConditions.titleIs("Login to All Conferencing - Conference Calls - Reliable Teleconferencing - Share Presentations, Participant Chat");
                break;
            case CORP:
                expectedCondition = ExpectedConditions.titleIs("Administrator Login");
                break;
            case PART:
                expectedCondition = ExpectedConditions.titleIs("Login to All Conferencing - Conference Calls - Reliable Teleconferencing - Share Presentations, Participant Chat");
                break;
        }
        return expectedCondition;
    }

    public void login(String clientID, String pwd) {
        LoginComponents loginComponents = new LoginComponents(driver);
        WebElement clientIDField = loginComponents.getClientIDField();
        clientIDField.click();
        clientIDField.sendKeys(new String(clientID));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement passwordField = loginComponents.getPasswordField();
        passwordField.click();
        passwordField.sendKeys(new String(pwd));
        passwordField.submit();

        WebDriverWait waitForMyAccount = new WebDriverWait(driver, 10);
        waitForMyAccount.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
    }

}
