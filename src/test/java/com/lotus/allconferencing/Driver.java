package com.lotus.allconferencing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 10/29/2017.
 */
public class Driver implements WebDriver {

    WebDriver driver;
    String browserName;

    public Driver(String browserName) {
        this.browserName = browserName;

        if(browserName.equalsIgnoreCase("chrome"))
            this.driver = BaseSeleniumTest.setDriver(BaseSeleniumTest.BrowserName.CHROME, false);

        if(browserName.equalsIgnoreCase("firefox"))
            this.driver = BaseSeleniumTest.setDriver(BaseSeleniumTest.BrowserName.FIREFOX, false);

        if(browserName.equalsIgnoreCase("ie"))
            this.driver = BaseSeleniumTest.setDriver(BaseSeleniumTest.BrowserName.IE, false);

        if(browserName.equalsIgnoreCase("phantomjs"))
            this.driver = BaseSeleniumTest.setDriver(BaseSeleniumTest.BrowserName.PHANTOMJS, false);
    }

    public void close() {
        this.driver.close();
    }

    public WebElement findElement(By arg0) {
        return this.driver.findElement(arg0);
    }

    public List<WebElement> findElements(By arg0) {
        return this.driver.findElements(arg0);
    }

    public void get(String arg0) {
        this.driver.get(arg0);
    }

    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    public String getPageSource() {
        return this.driver.getPageSource();
    }

    public String getTitle() {
        return this.driver.getTitle();
    }

    public String getWindowHandle() {
        return this.driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return this.driver.getWindowHandles();
    }

    public Options manage() {
        return this.driver.manage();
    }

    public Navigation navigate() {
        return this.driver.navigate();
    }

    public void quit() {
        this.driver.quit();
    }

    public TargetLocator switchTo() {
        return this.driver.switchTo();
    }

}
