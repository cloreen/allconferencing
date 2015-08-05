package com.lotus.allconferencing.website;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Created by Ben on 4/25/2015.
 */
public class LoginTests {
    private static WebDriver driver;
    private static WebDriverWait wait;

    public int getNumberOfElementsFound(By by) {
        return driver.findElements(by).size();
    }

    public WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public void acctLogin() {

    }

    @Before
    public void setup() {
        driver = new FirefoxDriver();
        driver.get("http://www.allconferencing.com/");
    }

    @Test
    public void basicLogin() {
        String homePageTitle = driver.getTitle();
        String loginPageTitle = "";
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        Boolean titleDisplays = false;

        List<WebElement> webElements;
        webElements = driver.findElements(locator);

        System.out.println(webElements.size());
        Boolean titleIs, clickability = false;
        int elementPosition = 0;
        int loginElementPosition = 0;
        for (WebElement webElement : webElements) {
            String elementText = webElement.getText();
            System.out.println("List Item Text = " + elementText);
            elementPosition++;
            if(elementText == "Account Login") {
                loginElementPosition = elementPosition;
            } else {
                continue;
            }
        }

        getElementWithIndex(locator, loginElementPosition).click();
        new WebDriverWait(driver, 10, 50).until(
                ExpectedConditions.invisibilityOfElementLocated(By.id("title"))
        );
/*
        switch (loginElementPosition) {
            case 0: acctLogin();
                break;
            case 1: corpLogin();
                break;
            case 2: partLogin();
                break;
        }
*/
        if (titleIs = true) {
            System.out.println("List item text was recognized.");
        } else {
            System.out.println("List item text was NOT recognized.");
        }

        loginPageTitle = driver.getTitle();

        driver.navigate().back();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Starting page title was: " + homePageTitle);
        System.out.println("Ending page title is: " + loginPageTitle);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}

