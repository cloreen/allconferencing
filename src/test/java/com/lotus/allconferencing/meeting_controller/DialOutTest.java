package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.meeting_controller.pages.LoginPageObject;
import com.lotus.allconferencing.webdriver.manager.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 8/9/2015.
 */
public class DialOutTest {
    private WebDriver driver;
    private String baseWindow, myAccountWindow;
    private LoginPageObject loginPage;

    public String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }

    public void getLoginPage(LoginPageObject.LoginType loginType) {

        System.out.println("Base window handle is: " + baseWindow);

        loginPage = new LoginPageObject(driver);
        //loginPage.get();
        loginPage.selectLogin(loginType);

        myAccountWindow = getWindow();
        loginPage.login("25784", "lotus456");
        //WebDriverWait waitForLoginPage = new WebDriverWait(driver, 10);
        //waitForLoginPage.until();
        System.out.println("My Account window handle is: " + myAccountWindow);

    }

    @Before
    public void setup() {
        Driver.set(Driver.BrowserName.FIREFOX);
        driver = Driver.get("http://www.allconferencing.com/");
        baseWindow = driver.getWindowHandle();
        getLoginPage(LoginPageObject.LoginType.STANDARD);
    }

    @Test
    public void dialOut() {
        // do nothing
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
