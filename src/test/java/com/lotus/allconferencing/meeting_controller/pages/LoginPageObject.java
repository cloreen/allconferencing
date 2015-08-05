package com.lotus.allconferencing.meeting_controller.pages;

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
public class LoginPageObject {
    private WebDriver driver;

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

    }

    public void get() {

        driver.get("http://www.allconferencing.com/");

    }


    public void selectLogin(LoginType loginType) {
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(By.cssSelector("ul[id='MenuBar3']>li>a"), loginType.value())).perform();
        actions.sendKeys(new String("w")).perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ExpectedCondition<Boolean> loginPageDisplays(LoginType loginType) {
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
        WebElement element = driver.findElement(By.cssSelector("form[id='login']>fieldset>input[type='text']"));
        element.click();
        element.sendKeys(new String(clientID));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElement(By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']"));
        element.click();
        element.sendKeys(new String(pwd));
        element.submit();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("title")));
    }

}
