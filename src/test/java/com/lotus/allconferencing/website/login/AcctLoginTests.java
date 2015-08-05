package com.lotus.allconferencing.website.login;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertFalse;

/**
 * Created by Ben on 5/1/2015.
 */
public class AcctLoginTests {

    private static WebDriver driver;
    private static String currentPage, loginPageTitle, acctLandingPageTitle = "";
    private static String homeURL = "http://www.allconferencing.com";
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    public WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public String[] checkPageTitles(By by, int pos) {

        String base = driver.getWindowHandle();

        System.out.println("Base window handle is: " + base);

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
        for (String item : set) {
            i++;
            System.out.println("Handle of Window " + i + " is: " + item);
            if (i == 1) {
                myHandle = item.toString();
            } else {
                otherHandle = item.toString();
            }
        }

        set.remove(base);
        assert (set.size() == 1);

        driver.switchTo().window(otherHandle);

        loginPageTitle = driver.getTitle();

        System.out.println(loginPageTitle);

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

        acctLandingPageTitle = driver.getTitle();

        String[] pageTitles = new String[2];
        pageTitles[0] = loginPageTitle;
        pageTitles[1] = acctLandingPageTitle;

        driver.close();
        driver.switchTo().window(base);

        return pageTitles;
    }


    public String[] getLoginMenuElementPosition(List<WebElement> webElementsList, int webElementsIndex) {
        int elementPosition = 0;
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();

        return checkPageTitles(locator, webElementsIndex);
    }



    @BeforeClass
    public static void setup() {
        driver = new FirefoxDriver();
        currentPage = driver.getCurrentUrl().toString();
        if(currentPage != homeURL) {
            driver.navigate().to(homeURL);
        } else {
            // do nothing
        }

        locator = By.cssSelector("ul[id='MenuBar3']>li>a");

        webElements = driver.findElements(locator);

        webElementsSize = webElements.size();
    }

    @Test
    public void acctLoginLink() {
        int webElementsIndex = 0;
        String loginPageTitle = "";
        String acctLandingPageTitle = "";
        String[] pageTitles = new String[2];

        pageTitles = getLoginMenuElementPosition(webElements, webElementsIndex);

        loginPageTitle = pageTitles[0];
        acctLandingPageTitle = pageTitles[1];

        System.out.println("Login Page Title is: " + loginPageTitle);
        System.out.println("Acct Landing Page Title is: " + acctLandingPageTitle);

        assertFalse("Account Login title and Landing Title do not match",
                loginPageTitle == acctLandingPageTitle);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
