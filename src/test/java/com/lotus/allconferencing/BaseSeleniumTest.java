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
    private static ReadPropertyFile readProps = null;

    private static ChromeDriverFactory chromeDriverFactory = new ChromeDriverFactory();

    public enum BrowserName {FIREFOX, CHROME, IE, OPERA, SAFARI, SAUCELABS, HTMLUNIT}

    public static WebDriver setDriver(BrowserName browser) {

        switch (browser) {
            case FIREFOX:
                driver = new FirefoxDriver();
                break;

            case CHROME:
                try {
                    driver = chromeDriverFactory.getChromeDriver();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ZipException e) {
                    e.printStackTrace();
                }
                break;

            case IE:
                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ieCapabilities.setCapability("requireWindowFocus", true);
                ieCapabilities.setCapability("ie.ensureCleanSession", true);

                driver = new InternetExplorerDriver(ieCapabilities);
                break;

            case OPERA:
                driver = new OperaDriver();
                break;

            case SAFARI:
                driver = new SafariDriver();
                break;

            case SAUCELABS:
                DesiredCapabilities sauceCapabilities = DesiredCapabilities.firefox();
                sauceCapabilities.setCapability("version", "5");
                sauceCapabilities.setCapability("platform", Platform.XP);
                try {
                    // add url to environment variables to avoid releasing with source
                    String sauceURL = System.getenv("SAUCELABS_URL");
                    driver = new RemoteWebDriver(
                            new URL(sauceURL),
                            sauceCapabilities);
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

    public static ReadPropertyFile getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProps;
    }
}
