package com.lotus.allconferencing.website.login;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.CorpAccountServicesComponents;
import com.lotus.allconferencing.services.components.OldAccountServicesComponents;
import com.lotus.allconferencing.services.pages.CorpAccountServicesPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.participantprojectshare.components.PartProjectShareComponents;
import com.lotus.allconferencing.services.participantprojectshare.pages.PartProjectSharePage;
import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.pages.HomePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ben on 5/1/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginFromHomePage {

    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;
    private static String currentPage, loginPageTitle, acctLandingPageTitle = "";
    private static String baseWindow, myAccountWindow;
    private static String homeURL = "http://www.allconferencing.com";
    private static WebElement acctSvcsTitle;
    private static String pageTitle;
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    private HomePage homePage = new HomePage(driver);
    //private HomePageComponents homePageComponents = new HomePageComponents(driver);
    //private LoginPageObject loginPage = new LoginPageObject(driver);
    private OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    private OldAccountServicesComponents oldAccountServicesComponents = new OldAccountServicesComponents(driver);
    private CorpAccountServicesPage corpAccountServicesPage = new CorpAccountServicesPage(driver);
    private CorpAccountServicesComponents corpAccountServicesComponents = new CorpAccountServicesComponents(driver);
    private PartProjectSharePage partProjectSharePage = new PartProjectSharePage(driver);
    private PartProjectShareComponents partProjectShareComponents = new PartProjectShareComponents(driver);

    @BeforeClass
    public static void setup() {

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver = new FirefoxDriver();

    }

    @Test
    public void test01_StandardLogin() {
        homePage.login(AccountType.LoginType.STANDARD, AccountType.AcctType.STANDARD_OLD);
        oldAccountServicesPage.logout();
        assertEquals("Title is as expected", homePage.EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test02_CorpLogin() {
        homePage.login(AccountType.LoginType.CORPORATE, AccountType.AcctType.CORPORATE);
        corpAccountServicesPage.logout();
    }

    @Test
    public void test03_ParticipantLogin() {
        goToHomePage();
//        login(LoginPageObject.LoginType.PART);
        partProjectSharePage.waitForTitle();
        partProjectSharePage.logout();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    //Methods-----------------------------------------------------------------------------------------------------------
    public void goToHomePage() {
        driver.get(readProps.getUrl());

        // Click on HTML element -- May be necessary to run tests in Firefox.
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        // Get handle for home page
        baseWindow = driver.getWindowHandle();
    }

    public static String getWindow() {
        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String windowHandle = "";
        //List<String> windowHandles = new ArrayList<String>();
        for (String item : set) {
            driver.switchTo().window(item);
        }
        windowHandle = driver.getWindowHandle();
        return windowHandle;
    }
/*
    public void login(LoginPageObject.LoginType loginType) {
        // Login with standard credentials, transfer driver to new window, bring My Account window to foreground,
        // get its handle.
        //System.out.println("Base window handle is: " + baseWindow);

  //      loginPage = new LoginPageObject(driver);
//        loginPage.selectLogin(loginType);
        myAccountWindow = getWindow();
        switch (loginType) {
            case STANDARD:
                loginPage.login(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword());
                break;
            case CORP:
                loginPage.login(readProps.getCorpClientID(), readProps.getCorpPassword());
                break;
            case PART:
                loginPage.login(readProps.getParticipantClientID(), readProps.getParticipantAcctPwd());
                break;
            default:
                System.out.println("A proper login account type must be specified.");
                System.exit(-1);
        }

        //System.out.println("My Account window handle is: " + myAccountWindow);
    }
    */
}
