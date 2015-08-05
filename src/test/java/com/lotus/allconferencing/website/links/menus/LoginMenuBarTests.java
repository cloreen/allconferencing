package com.lotus.allconferencing.website.links.menus;

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

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Ben on 5/1/2015.
 */
public class LoginMenuBarTests {

    private static WebDriver driver;
    private static String currentPage, homePageTitle, newPageTitle, newPageContent = "";
    private static String homeURL = "http://www.allconferencing.com";
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    public WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public String checkNewPageTitle(By by, int pos) {

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

        newPageTitle = driver.getTitle();

        System.out.println(newPageTitle);

        driver.close();
        driver.switchTo().window(base);

        return newPageTitle;
    }


    public String getLoginMenuElementPosition(List<WebElement> webElementsList, int webElementsIndex) {
        int elementPosition = 0;
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();

        return checkNewPageTitle(locator, webElementsIndex);
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

        homePageTitle = driver.getTitle();
        locator = By.cssSelector("ul[id='MenuBar3']>li>a");

        webElements = driver.findElements(locator);

        webElementsSize = webElements.size();
    }

    @Test
    public void acctLoginLink() {
        int webElementsIndex = 0;
        String newPageTitle = "";

        newPageTitle = getLoginMenuElementPosition(webElements, webElementsIndex);

        assertEquals("The login page title matches the index",
                "Login to All Conferencing - Conference Calls - Reliable Teleconferencing" +
                        " - Share Presentations, Participant Chat", newPageTitle);
    }

    @Test
    public void corpLoginLink() {
        int webElementsIndex = 1;
        String newPageTitle = "";

        newPageTitle = getLoginMenuElementPosition(webElements, webElementsIndex);

        assertEquals("The Corp Login page title matches index", "Administrator " +
                "Login", newPageTitle);
    }

    @Test
    public void partLoginLink() {
        int webElementsIndex = 2;
        String newPageTitle = "";

        newPageTitle = getLoginMenuElementPosition(webElements, webElementsIndex);

        assertEquals("The login page title matches the index",
                "Login to All Conferencing - Conference Calls - Reliable Teleconferencing" +
                        " - Share Presentations, Participant Chat", newPageTitle);
    }


    @AfterClass
    public void tearDown() {
        driver.close();
    }
}
