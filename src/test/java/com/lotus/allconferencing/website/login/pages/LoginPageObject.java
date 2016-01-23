package com.lotus.allconferencing.website.login.pages;

import com.lotus.allconferencing.PageManager;
import com.lotus.allconferencing.ReadPropertyFile;
import com.lotus.allconferencing.services.pages.CorpAccountServicesPage;
import com.lotus.allconferencing.services.pages.NewAccountServicesPage;
import com.lotus.allconferencing.services.pages.OldAccountServicesPage;
import com.lotus.allconferencing.services.pages.SimpleAccountServicesPage;
import com.lotus.allconferencing.website.login.components.LoginComponents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Created by Ben on 7/20/2015.
 */

/*******
 *
 */

public class LoginPageObject extends PageManager {
    private WebDriver driver;
//    private HomePage.LoginType accountType;
    private ReadPropertyFile readProps = null;



    // Selectors for Login Components-----------------------------------------------------------------------------------
    private static By ACCT_BUTTON = By.cssSelector("ul[id='MenuBar3']>li>a");
    private static By CLIENT_ID = By.cssSelector("form[id='login']>fieldset>input[name='txtClientId']");
    //private static By CLIENT_ID = By.cssSelector("input[name='txtClientId']");
    private static By PASSWORD = By.cssSelector("form[id='login']>fieldset>label>label>input[type='password']");
    private static By CORP_CLIENT_ID = By.id("CtrlLoginInfo1_username");
    private static By CORP_PASSWORD = By.id("CtrlLoginInfo1_Password1");
    private static By CORP_LOGIN_BUTTON = By.id("CtrlLoginInfo1_Submit");
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

    public LoginPageObject(WebDriver newDriver, AccountType.LoginType loginType, AccountType.AcctType acctType) {
        driver = newDriver;
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Title of Login Page is: " + driver.getTitle());
        //PageFactory.initElements(driver, this);
        serveLogin(loginType, acctType);
    }


    public void get() {

        driver.get(readProps.getUrl());

    }


    public void selectLogin(LoginType loginType) {
        LoginComponents loginComponents = new LoginComponents(driver);
//        accountType = loginType;
        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(loginComponents.getAcctButtonBy(), loginType.value())).perform();
        actions.sendKeys(new String("w")).perform();// Opens new page in a new window (contextClick() + sendKeys("w") = open in new window)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
/*
    private static ExpectedCondition<Boolean> loginPageDisplays(HomePage.LoginType loginType) {
        ExpectedCondition<Boolean> expectedCondition = null;

        switch(loginType) {
            case STANDARD:
                expectedCondition = ExpectedConditions.titleIs("Login to All Conferencing - Conference Calls - Reliable Teleconferencing - Share Presentations, Participant Chat");
                break;
            case CORP:
                expectedCondition = ExpectedConditions.titleIs("Administrator Login");
                break;
            case PART:
                expectedCondition = ExpectedConditions.titleIs("Login to All Conferencing - Conference Calls - Reliable Teleconferencing - Share Presentations, Participant Chat");
                break;
        }
        return expectedCondition;
    }
*/
    public void serveLogin(AccountType.LoginType loginType, AccountType.AcctType acctType) {
        switch (loginType) {
            case CORPORATE:
                loginToCorpAcct();
                break;
            case STANDARD:
                loginToStandard(acctType);
                break;
            case PARTICIPANT:
                loginToStandard(acctType);
                break;
        }
    }

    private SimpleAccountServicesPage loginToSimpleStandardAcct() {
        commonStandardLogin(readProps.getOldAcctClientID(), readProps.getOldAcctPassword());
        return new SimpleAccountServicesPage(driver);
    }

    public OldAccountServicesPage loginToOldStandardAcct() {
        commonStandardLogin(readProps.getOlderAcctClientID(), readProps.getOlderAcctPassword());
        return new OldAccountServicesPage(driver);
    }


    public CorpAccountServicesPage loginToCorpAcct() {
        WebElement clientIDField, passwordField;
        clientIDField = driver.findElement(CORP_CLIENT_ID);
        clientIDField.click();
        clientIDField.sendKeys(new String(readProps.getCorpClientID()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passwordField = driver.findElement(CORP_PASSWORD);
        passwordField.click();
        passwordField.sendKeys(new String(readProps.getCorpPassword()));
        driver.findElement(CORP_LOGIN_BUTTON).click();
        waitForTitle(driver);
        return new CorpAccountServicesPage(driver);
    }


    public void loginToStandard(AccountType.AcctType acctType) {
        switch (acctType) {
            case STANDARD_OLD:
                loginToOldStandardAcct();
                break;
            case STANDARD_SIMPLE:
                loginToSimpleStandardAcct();
                break;
            case STANDARD_NEW:
                loginToNewStandardAcct();
                break;
        }
    }

    private NewAccountServicesPage loginToNewStandardAcct() {
        commonStandardLogin(readProps.getOwnerClientID(), readProps.getOwnerPassword());
        return new NewAccountServicesPage(driver);
    }

    public void commonStandardLogin(String clientID, String password) {
        WebElement clientIDField, passwordField;
        clientIDField = driver.findElement(CLIENT_ID);
        clientIDField.click();
        clientIDField.sendKeys(new String(clientID));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passwordField = driver.findElement(PASSWORD);
        passwordField.click();
        passwordField.sendKeys(new String(password));
        passwordField.submit();
        waitForTitle(driver);
    }
    /*
    public WebDriverWait waitForTitle() {
        WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
        waitForTitle.until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("title"))
        );
        return waitForTitle;
    }
    */
    /*
    //Need to refine way of getting proper clientID and password for given test
    public StringArray getLoginCreds(LoginType loginType) {
        switch (loginType) {

        }
    }
    */
}
