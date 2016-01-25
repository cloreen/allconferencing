package com.lotus.allconferencing.website.login.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.CorpAccountServicesPage;
import com.lotus.allconferencing.services.pages.NewAccountServicesPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.pages.SimpleAccountServicesPage;
import com.lotus.allconferencing.services.participantprojectshare.pages.PartProjectSharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 7/20/2015.
 */

/*******
 *
 */

public class LoginPageObject extends PageManager {
    private WebDriver driver;
    private ReadPropertyFile readProps = null;

    // Selectors for Login Components-----------------------------------------------------------------------------------
    private static By ACCT_BUTTON = By.cssSelector("ul[id='MenuBar3']>li>a");
    private static By CLIENT_ID = By.cssSelector("form[id='login']>fieldset>input[name='txtClientId']");
    private static By PASSWORD = By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']");
    private static By CORP_CLIENT_ID = By.id("CtrlLoginInfo1_username");
    private static By CORP_PASSWORD = By.id("CtrlLoginInfo1_Password1");
    private static By CORP_LOGIN_BUTTON = By.id("CtrlLoginInfo1_Submit");
    public static String CORP_LOGIN_EXPECTED_TITLE = "Administrator Login";
    public static String LOGIN_EXPECTED_TITLE = "Login to All Conferencing - Conference Calls - Reliable Teleconferencing - Share Presentations, Participant Chat";
    //------------------------------------------------------------------------------------------------------------------

    private WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public enum LoginType {
        STANDARD(0), CORP(1), PART(2);

        private int loginIndex;

        LoginType(int value) { this.loginIndex = value; }

        public int value() { return loginIndex; }
    }

    public enum AccessType {
        DISPLAY(0), LOGIN(1), NOPASS(2), INVALIDPASS(3), NOCLIENTID(4), INVALIDCLIENTID(5);

        private int accessType;

        AccessType(int value) {
            this.accessType = value;
        }
        public int value() {
            return accessType;
        }
    }

    public LoginPageObject(WebDriver newDriver) {
        driver = newDriver;
    }

    public LoginPageObject(WebDriver newDriver, AccountType.LoginType loginType, AccountType.AcctType acctType, AccessType accessType) {
        driver = newDriver;
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(accessType != AccessType.DISPLAY) {
            serveLogin(loginType, acctType, accessType);
        } else {
            // Just display the page to assert on title within the test.
        }
    }


    public void get() {

        driver.get(readProps.getUrl());

    }


    public void selectLogin(LoginType loginType) {
        driver.findElement((By) getElementWithIndex(ACCT_BUTTON, loginType.value())).click();
        /*
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(ACCT_BUTTON, loginType.value())).perform();
        actions.sendKeys(new String("w")).perform();// Opens new page in a new window (contextClick() + sendKeys("w") = open in new window)
        */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void serveLogin(AccountType.LoginType loginType, AccountType.AcctType acctType, AccessType accessType) {
        switch (loginType) {
            case CORPORATE:
                loginToCorpAcct(accessType);
                break;
            case STANDARD:
                loginToStandard(acctType, accessType);
                break;
            case PARTICIPANT:
                loginToStandard(acctType, accessType);
                break;
        }
    }

    private SimpleAccountServicesPage loginToSimpleStandardAcct(AccessType accessType) {
        commonStandardLogin(readProps.getOldAcctClientID(), readProps.getOldAcctPassword(), accessType);
        return new SimpleAccountServicesPage(driver);
    }

    public OldAccountServicesPage loginToOldStandardAcct(AccessType accessType) {
        commonStandardLogin(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword(), accessType);
        return new OldAccountServicesPage(driver);
    }

    public void invalidLoginToOldStandardAcct(AccessType accessType) {
        commonStandardLogin(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword(), accessType);
    }

    private NewAccountServicesPage loginToNewStandardAcct(AccessType accessType) {
        commonStandardLogin(readProps.getOwnerClientID(), readProps.getOwnerPassword(), accessType);
        return new NewAccountServicesPage(driver);
    }

    private PartProjectSharePage loginToParticipantAcct(AccessType accessType) {
        commonStandardLogin(readProps.getParticipantClientID(), readProps.getParticipantAcctPwd(), accessType);
        return new PartProjectSharePage(driver);
    }

    public CorpAccountServicesPage loginToCorpAcct(AccessType accessType) {
        WebElement clientIDField, passwordField;
        if (accessType != AccessType.NOCLIENTID) {
            clientIDField = driver.findElement(CORP_CLIENT_ID);
            clientIDField.click();
            if (accessType != AccessType.INVALIDCLIENTID) {
                clientIDField.sendKeys(new String(readProps.getCorpClientID()));
            } else {
                clientIDField.sendKeys(new String(readProps.getCorpClientID() + "123"));
            }
        } else {
            // Leave Client ID blank. Enter password below.
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(accessType != AccessType.NOPASS) {
            passwordField = driver.findElement(CORP_PASSWORD);
            passwordField.click();
            if (accessType != AccessType.INVALIDPASS) {
                passwordField.sendKeys(new String(readProps.getCorpPassword()));
            } else {
                passwordField.sendKeys(new String(readProps.getCorpPassword() + "123"));
            }
        } else {
            // Leave Password blank. Submit invalid login.
        }

        driver.findElement(CORP_LOGIN_BUTTON).click();
        try {
            waitForTitle(driver);
        } catch (WebDriverException wde) {
            System.out.println("Failed login alert raised exception!");
        }
        return new CorpAccountServicesPage(driver);
    }


    public void loginToStandard(AccountType.AcctType acctType, AccessType accessType) {
        switch (acctType) {
            case STANDARD_OLD:
                if(accessType == AccessType.LOGIN) {
                    loginToOldStandardAcct(accessType);
                } else {
                    invalidLoginToOldStandardAcct(accessType);
                }
                break;
            case STANDARD_SIMPLE:
                loginToSimpleStandardAcct(accessType);
                break;
            case STANDARD_NEW:
                loginToNewStandardAcct(accessType);
                break;
            case PARTICIPANT:
                loginToParticipantAcct(accessType);
                break;
        }
    }

    public void commonStandardLogin(String clientID, String password, AccessType accessType) {
        WebElement clientIDField, passwordField;
        if(accessType != AccessType.NOCLIENTID) {
            clientIDField = driver.findElement(CLIENT_ID);
            clientIDField.click();
            if (accessType != AccessType.INVALIDCLIENTID) {
                clientIDField.sendKeys(new String(clientID));
            } else {
                clientIDField.sendKeys(new String(clientID) + "123");
            }
        } else {
            // Leave Client ID blank. Continue with password below.
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passwordField = driver.findElement(PASSWORD);
        if(accessType != AccessType.NOPASS) {
            passwordField.click();
            if (accessType != AccessType.INVALIDPASS) {
                passwordField.sendKeys(new String(password));
            } else {
                passwordField.sendKeys(new String(password) + "123");
            }
        } else {
            // Leave Password blank. Continue with invalid login submission below.
        }

        passwordField.submit();

        try {
            waitForTitle(driver);
        } catch (WebDriverException wde) {
            System.out.println("Failed login alert raised exception!");
        }

    }

}
