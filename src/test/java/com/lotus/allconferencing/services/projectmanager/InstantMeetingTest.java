package com.lotus.allconferencing.services.projectmanager;

import com.lotus.allconferencing.Login;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 5/3/2015.
 */
public class InstantMeetingTest extends Login {

    private static WebDriver driver;
    private static String currentPage, loginPageTitle, acctLandingPageTitle = "";
    private static String homeURL = "http://www.allconferencing.com/";
    private static List<WebElement> webElements;
    private static int webElementsSize;
    public static By locator;
    //private static By locator;

    public static WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public static void checkLoginPageTitles(By by, int pos) {

        String base = driver.getWindowHandle();

        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(by, pos)).perform();
        actions.sendKeys(new String("w")).perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String myHandle = "";
        String otherHandle = "";

        set.remove(base);
        assert (set.size() == 1);
        if (set.size() == 1) {
            System.out.println("Set size is 1");
        } else {
            System.out.println("Set size is " + set.size());
        }

        driver.switchTo().window(otherHandle);

        WebElement element = driver.findElement(By.cssSelector("form[id='login']>fieldset>input[type='text']"));
        element.click();
        element.sendKeys(new String("25784"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElement(By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']"));
        element.click();
        element.sendKeys(new String("lotus456"));
        element.submit();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();
        driver.switchTo().window(base);

    }


    public static void acctLogin(List<WebElement> wElements) {
        int webElementsIndex = 0;
        WebElement element = webElements.get(webElementsIndex);
        checkLoginPageTitles(locator, webElementsIndex);
    }



    @Test
    public void instantMeeting() {
        driver = new FirefoxDriver();
        currentPage = driver.getCurrentUrl().toString();
        if(currentPage != homeURL) {
            driver.navigate().to(homeURL + "pages-conference-calls/login.asp");
        } else {
            // do nothing
        }

        locator = By.cssSelector("ul[id='MenuBar3']>li>a");

        webElements = driver.findElements(locator);

        webElementsSize = webElements.size();

        acctLoginLink(webElements, 0);

        driver.quit();

    }


}
