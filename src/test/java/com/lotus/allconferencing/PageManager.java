package com.lotus.allconferencing;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class PageManager {
    private static WebDriver driver;
    private static Boolean isInvalid;
    private ReadPropertyFile readProps;

    public PageManager(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static void getSeleniumAlert(WebDriver driver, Integer version, String modPass) {
        //When using PhantomJS, don't us switchTo() to handle alears
        //Get ConfID of scheduled conference
        WebElement deleteButton = driver.findElement(By.cssSelector("input[name='cmdRemove']"));
        String onClickValue = deleteButton.getAttribute("onclick");
        System.out.println("This is the onclick() value for the Delete Button: " + onClickValue);
        List<String> onClickList = Arrays.asList(onClickValue.split(","));
//        onClickList.toArray();
        String confIDStr = onClickList.get(2);
        System.out.println("The ConfID part of the onclick() value is:" + confIDStr);
        String confID = confIDStr.replace("\"", "");
        System.out.println("The stripped-down ConfID is:" + confID);
        if (ExcelData.browser == ExcelData.Browser.valueOf("PHANTOMJS")) {
            try {
                System.out.println("Inside getSeleniumAlert()!");
                /*
                JavascriptExecutor js1 = (JavascriptExecutor) driver;
                js1.executeScript("var page = this;" +
                        "page.onError = function(msg, trace) {" +
                        "console.log(msg);" +
                        "trace.forEach(function(item)) {" +
                        "console.log(' ', item.file, ':', item.line);" +
                        "});" +
                        "};");
                System.out.println("JavascriptExecutor executed.");
                */
            /*JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("window.confirm = function(){return true;}");*/
                if (version == 2) {
//                    ((PhantomJSDriver)driver).executePhantomJS("document.frmEdit.txtConfID.value = \"" + confID + "\";" +
                    JavascriptExecutor js = (JavascriptExecutor)driver;
                    js.executeScript("document.frmEdit.txtConfID.value = \"" + confID + "\";" +
                            "document.frmEdit.txtID.value = \"4608\";" +
                            "document.frmEdit.txtTZ.value = \"PACIFIC\";" +
                            "document.frmEdit.txtMPassCode.value = \"" + modPass + "\";" +
                            "document.frmEdit.txtConfName.value = \"v2+Test+Meeting+%2D+Old+Scheduler\";" +
                            "document.frmEdit.txtRecc.value = \"F\";" +
                            "var doc = document.forms[\"frmEdit\"];" +
                            "doc.action = \"Conference_delete_do.asp?ver=2\";" +
                            "doc.submit();");
//                    System.out.println("PhantomJSJavascriptExecutor executed.");
                    System.out.println("JavascriptExecutor executed.");
                } else {
                    System.out.println("The wrong version was passed to getSeleniumAlert().");
                }


                /*
                ((PhantomJSDriver) driver).executePhantomJS("var page = this;" +
                        "page.onConfirm = function(msg) {" +
                        "console.log('CONFIRM: ' + msg);return true;" +
                        "};");
                */

            } catch (Exception e) {
                LogEntries logs = driver.manage().logs().get("browser");
                logs.getAll();
                for (Object log : logs) {
                    System.out.println(log.toString());
                }
                System.out.println("Inside getSeleniumAlert() - Exception caught.");
                e.printStackTrace();
            }

        } else {
            try {
                    System.out.println("Inside getSeleniumAlert() . The browser is not PhantomJS.");
            } catch (NoAlertPresentException nape) {
                    System.out.println("Inside getSeleniumAlert() - NoAlertPresentException caught.");
            }

        }
        /*
        if (pageManagerAlert != null) {
            return pageManagerAlert;
        } else {
            return null;
        }
        */
    }

    public static void confirmSeleniumAlert() {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.confirm = function() {return true;}");

    }



    /*================================================================================================
    CUSTOM SYNCHRONIZATION----------------------------------------------------------------------------
    =================================================================================================*/

    public static WebDriverWait waitForTitle(WebDriver pgMgrDriver/*, AccountType.AcctType acctType*/) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        String expectedURL = new String("");
        String newTitle = new String("");
        WebDriverWait waitForURL = new WebDriverWait(driver, 10);
        WebDriverWait waitForTitleChange = new WebDriverWait(driver, 10);
/*
        switch(acctType) {
            case STANDARD_OLD:
                expectedURL = "https://www.allconferencing.com/pages-conference-calls/account_services.asp?Rights=0";
                break;
            case STANDARD_NEW:
                expectedURL = "https://www.allconferencing.com/pages-conference-calls/welcome.asp?Rights=0";
                break;
            case STANDARD_SIMPLE:
                expectedURL = "https://www.allconferencing.com/pages-conference-calls/services.asp?Rights=0";
                break;
            case CORPORATE:
                expectedURL = "https://www.allconferencing.com/CBM/(cucryenrk43f0w55vgg50a45)/CBMUI/ASPPages/DisplayAccountList.aspx";
                break;
        }

        waitForURL.until(
                ExpectedConditions.urlToBe(expectedURL)
        );
*/
        final String originalPageTitle = driver.getTitle();
        waitForTitleChange.until(
                new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        Boolean driverResult = false;
                        if(webDriver.getTitle() != null) {
                            if(webDriver.getTitle() != originalPageTitle) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
        );

        return waitForTitleChange;
    }

    public ExpectedCondition<Boolean> titleIsNot(final String titleText) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                Boolean driverResult = false;
                if(driver.getTitle() != null) {
                    if(driver.getTitle() != titleText) {
                        driverResult = true;
                    } else {
                        driverResult = false;
                    }
                } else {
                    driverResult = false;
                }
                return driverResult;
            }
        };
    }

    public static FluentWait waitForTitleFluently(WebDriver pgMgrDriver) /*throws WebDriverException*/ {
        WebDriver driver = pgMgrDriver;
        FluentWait waitForTitleDisappearance = new WebDriverWait(driver, 30);
        waitForTitleDisappearance
                .pollingEvery(10, TimeUnit.MILLISECONDS)
                .withTimeout(20, TimeUnit.SECONDS)
                .until(
                        ExpectedConditions.invisibilityOfElementLocated(By.tagName("title"))
                );

        FluentWait waitForTitleFluently = new WebDriverWait(driver, 10);
        waitForTitleFluently.
                pollingEvery(100, TimeUnit.MILLISECONDS).
                withTimeout(5, TimeUnit.SECONDS).
                until(
                        ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
                );

        return waitForTitleDisappearance;
    }

    public static WebDriverWait waitForPageToChange(WebDriver pgMgrDriver, String oldTitle) {
        WebDriver driver = pgMgrDriver;
        String prevPageTitle = oldTitle;
        WebDriverWait waitForPageChange = new WebDriverWait(driver, 10);
        waitForPageChange.until(
                ExpectedConditions.not(
                        ExpectedConditions.titleIs(prevPageTitle)
                )
        );
        return waitForPageChange;
    }

}
