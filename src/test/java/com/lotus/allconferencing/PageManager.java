package com.lotus.allconferencing;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class PageManager {
    private static WebDriver driver;
    private static Boolean isInvalid;

    public PageManager() {
    }

    public static String getNewWindow(WebDriver winMgrDriver, Set<String> windowHandles) {
        WebDriver driver = winMgrDriver;
        Set<String> set = windowHandles;
        String windowHandle = "";
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection)url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

    public static Alert getSeleniumAlert(WebDriver driver) {
        //When using PhantomJS, don't us switchTo() to handle alears
        try {
            if (ExcelData.browser == ExcelData.Browser.valueOf("PHANTOMJS")) {

                JavascriptExecutor js1 = (JavascriptExecutor) driver;
                js1.executeScript("var page = this;" +
                        "page.onError = function(msg, trace) {" +
                        "console.log(msg);" +
                        "trace.forEach(function(item)) {" +
                        "console.log(' ', item.file, ':', item.line);" +
                        "});" +
                        "};");
            /*JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("window.confirm = function(){return true;}");*/
                ((PhantomJSDriver) driver).executePhantomJS("var page = this;" +
                        "page.onConfirm = function(msg) {" +
                        "console.log('CONFIRM: ' + msg);return true;" +
                        "};");
                System.out.println("Inside getSeleniumAlert()!");

            } else {
                try {
                    System.out.println("Inside getSeleniumAlert(). The browser is not PhantomJS.");
                    return driver.switchTo().alert();
                } catch (NoAlertPresentException nape) {
                    return null;
                }
            }
        } catch (Exception e) {
            LogEntries logs = driver.manage().logs().get("browser");
            logs.getAll();
            System.out.print(logs);
        }
        return null;
    }

    public static void confirmSeleniumAlert() {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.confirm = function() {return true;}");

    }



    /*================================================================================================
    CUSTOM SYNCHRONIZATION----------------------------------------------------------------------------
    =================================================================================================*/

    public static WebDriverWait waitForTitle(WebDriver pgMgrDriver) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
        waitForTitle.until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
        );

        return waitForTitle;
    }

    public static FluentWait waitForTitleFluently(WebDriver pgMgrDriver) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        FluentWait waitForTitleFluently = new WebDriverWait(driver, 10);
        waitForTitleFluently.
                pollingEvery(100, TimeUnit.MILLISECONDS).
                withTimeout(5, TimeUnit.SECONDS).
                until(
                        ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
                );

        return waitForTitleFluently;
    }

    public static WebDriverWait waitForPageChange(WebDriver pgMgrDriver) {
        WebDriver driver = pgMgrDriver;
        WebDriverWait waitForPageChange = new WebDriverWait(driver, 10);
        waitForPageChange.until(
                ExpectedConditions.invisibilityOfElementLocated(By.tagName("title"))
        );
        return waitForPageChange;
    }

}
