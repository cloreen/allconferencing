package com.lotus.allconferencing;

import net.lingala.zip4j.exception.ZipException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class BaseSeleniumTest {
    private static WebDriver driver;

    private static ChromeDriverFactory chromeDriverFactory = new ChromeDriverFactory();

    public enum BrowserName {FIREFOX, CHROME, IE, OPERA, SAFARI, SAUCELABS, HTMLUNIT}

    public static WebDriver setDriver(BrowserName browser) throws ZipException, IOException {

        switch (browser) {
            case FIREFOX:
                driver = new FirefoxDriver();
                break;

            case CHROME:
                driver = chromeDriverFactory.getChromeDriver();
                break;

            case IE:
                driver = new InternetExplorerDriver();
                break;

            case OPERA:
                driver = new OperaDriver();
                break;

            case SAFARI:
                driver = new SafariDriver();
                break;

            case SAUCELABS:
                DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                capabilities.setCapability("version", "5");
                capabilities.setCapability("platform", Platform.XP);
                try {
                    // add url to environment variables to avoid releasing with source
                    String sauceURL = System.getenv("SAUCELABS_URL");
                    driver = new RemoteWebDriver(
                            new URL(sauceURL),
                            capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case HTMLUNIT:
                driver = new HtmlUnitDriver();
                break;
        }
        return driver;
    }

    public static WebDriver getDriver() {

        if(driver != null) {
            return driver;
        }

        return new FirefoxDriver();
    }
}
