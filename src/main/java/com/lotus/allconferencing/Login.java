package com.lotus.allconferencing;

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
public class Login  {
    private static WebDriver aDriver;
    private static String currentPage, loginPageTitle, acctLandingPageTitle = "";
    private static String homeURL = "http://www.allconferencing.com";
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    public static WebElement getElementWithIndex(By by, int pos) {
        return aDriver.findElements(by).get(pos);
    }

    public static String[] checkPageTitles(By by, int pos) {

        String base = aDriver.getWindowHandle();

        System.out.println("Base window handle is: " + base);

        Actions actions = new Actions(aDriver);
        actions.contextClick(getElementWithIndex(by, pos)).perform();
        actions.sendKeys(new String("w")).perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        Set<String> set = aDriver.getWindowHandles();
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

        aDriver.switchTo().window(otherHandle);

        loginPageTitle = aDriver.getTitle();

        System.out.println(loginPageTitle);

        WebElement element = aDriver.findElement(By.cssSelector("form[id='login']>fieldset>input[type='text']"));
        element.click();
        element.sendKeys(new String("25784"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = aDriver.findElement(By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']"));
        element.click();
        element.sendKeys(new String("lotus456"));
        element.submit();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        acctLandingPageTitle = aDriver.getTitle();

        String[] pageTitles = new String[2];
        pageTitles[0] = loginPageTitle;
        pageTitles[1] = acctLandingPageTitle;

        aDriver.close();
        aDriver.switchTo().window(base);

        return pageTitles;
    }


    public static String[] getLoginMenuElementPosition(List<WebElement> webElementsList, int webElementsIndex) {
        int elementPosition = 0;
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();

        return checkPageTitles(locator, webElementsIndex);
    }



    public static void loginSetup(WebDriver aDriver) {
        aDriver = new FirefoxDriver();
        currentPage = aDriver.getCurrentUrl().toString();
        if(currentPage != homeURL) {
            aDriver.navigate().to(homeURL);
        } else {
            // do nothing
        }

        locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        int webElementsIndex = 0;

        webElements = aDriver.findElements(locator);

        webElementsSize = webElements.size();

        acctLoginLink(webElements, webElementsIndex);

    }

    public static void acctLoginLink(List<WebElement> webElements, int webElementsIndex) {
        String loginPageTitle = "";
        String acctLandingPageTitle = "";
        String[] pageTitles = new String[2];

        pageTitles = getLoginMenuElementPosition(webElements, webElementsIndex);

        loginPageTitle = pageTitles[0];
        acctLandingPageTitle = pageTitles[1];

        System.out.println("Login Page Title is: " + loginPageTitle);
        System.out.println("Acct Landing Page Title is: " + acctLandingPageTitle);
    }
}


