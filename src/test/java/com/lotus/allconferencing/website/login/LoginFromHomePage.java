package com.lotus.allconferencing.website.login;

import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.components.CorpAccountServicesComponents;
import com.lotus.allconferencing.services.components.OldAccountServicesComponents;
import com.lotus.allconferencing.services.pages.CorpAccountServicesPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.participantprojectshare.components.PartProjectShareComponents;
import com.lotus.allconferencing.services.participantprojectshare.pages.PartProjectSharePage;
import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
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

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ben on 5/1/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginFromHomePage {

    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;
    private static LoginPageObject.AccessType accessType;
    private static AccountType.AcctType accountType;
    private static AccountType.LoginType loginType;
    private static String currentPage, loginPageTitle, acctLandingPageTitle = "";
    private static String baseWindow, myAccountWindow;
    private static String homeURL = "http://www.allconferencing.com";
    private static WebElement acctSvcsTitle;
    private static String pageTitle;
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    private HomePage homePage = new HomePage(driver);
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
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.LOGIN);
        oldAccountServicesPage.logout();
        assertEquals("Title is as expected", homePage.EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test02_CorpLogin() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.LOGIN);
        corpAccountServicesPage.logout();
        LoginPageObject loginPage = new LoginPageObject(driver, loginType.CORPORATE, accountType.CORPORATE, accessType.DISPLAY);
        assertEquals("Title is as expected", loginPage.CORP_LOGIN_EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test03_ParticipantLogin() {
        homePage.login(loginType.PARTICIPANT, accountType.PARTICIPANT, accessType.LOGIN);
        partProjectSharePage.logout();
        assertEquals("Title is as expected", homePage.EXPECTED_TITLE, driver.getTitle());
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
