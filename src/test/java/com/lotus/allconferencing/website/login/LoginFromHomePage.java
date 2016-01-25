package com.lotus.allconferencing.website.login;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.CorpAccountServicesPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.participantprojectshare.pages.PartProjectSharePage;
import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;
import com.lotus.allconferencing.website.pages.HomePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Ben on 5/1/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginFromHomePage extends BaseSeleniumTest {

    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;
    private static LoginPageObject.AccessType accessType;
    private static AccountType.AcctType accountType;
    private static AccountType.LoginType loginType;

    private HomePage homePage = new HomePage(driver);
    private LoginPageObject loginPage = new LoginPageObject(driver);
    private OldAccountServicesPage oldAccountServicesPage = new OldAccountServicesPage(driver);
    private CorpAccountServicesPage corpAccountServicesPage = new CorpAccountServicesPage(driver);
    private PartProjectSharePage partProjectSharePage = new PartProjectSharePage(driver);

    @BeforeClass
    public static void setup() {

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        driver = setDriver(BrowserName.FIREFOX);

    }

    @Test
    public void test01_StandardLogin() {
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.LOGIN);
        oldAccountServicesPage.logout();
        assertEquals("Title is as expected", homePage.EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test02_StandardLoginNoClientID() {
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.NOCLIENTID);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test03_StandardLoginNoPass() {
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.NOPASS);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test04_StandardLoginWrongClientID() {
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.INVALIDCLIENTID);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test05_StandardLoginWrongPass() {
        homePage.login(loginType.STANDARD, accountType.STANDARD_OLD, accessType.INVALIDPASS);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test06_CorpLogin() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.LOGIN);
        corpAccountServicesPage.logout();
        LoginPageObject loginPage = new LoginPageObject(driver, loginType.CORPORATE, accountType.CORPORATE, accessType.DISPLAY);
        assertEquals("Title is as expected", loginPage.CORP_LOGIN_EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test07_CorpLoginNoClientID() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.NOCLIENTID);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.CORP_LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test08_CorpLoginNoPassword() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.NOPASS);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.CORP_LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test09_CorpLoginWrongClientID() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.INVALIDCLIENTID);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.CORP_LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test10_CorpLoginWrongPassword() {
        homePage.login(loginType.CORPORATE, accountType.CORPORATE, accessType.INVALIDPASS);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.CORP_LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test11_ParticipantLogin() {
        homePage.login(loginType.PARTICIPANT, accountType.PARTICIPANT, accessType.LOGIN);
        partProjectSharePage.logout();
        assertEquals("Title is as expected", homePage.EXPECTED_TITLE, driver.getTitle());
    }

    @Test
    public void test12_ParticipantLoginWrongClientID() {
        homePage.login(loginType.PARTICIPANT, accountType.PARTICIPANT, accessType.INVALIDCLIENTID);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @Test
    public void test13_ParticipantLoginWrongPassword() {
        homePage.login(loginType.PARTICIPANT, accountType.PARTICIPANT, accessType.INVALIDPASS);
        assertTrue("Login is unsuccessful", driver.getTitle().equals(loginPage.LOGIN_EXPECTED_TITLE));
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
