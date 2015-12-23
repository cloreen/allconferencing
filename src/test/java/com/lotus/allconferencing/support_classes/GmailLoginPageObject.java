package com.lotus.allconferencing.support_classes;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ben on 11/27/2015.
 */
public class GmailLoginPageObject {
    private WebDriver driver;
    private ReadPropertyFile readProps = null;
    private WebElement username;
    private WebElement password;

    public GmailLoginPageObject (WebDriver newDriver) {
        driver = newDriver;
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void goToGmail() {
        this.driver.get("http://www.gmail.com/");
        waitForGmail();
    }

    public void enterGmailUsername() {
        username = driver.findElement(By.cssSelector("input[id='Email']"));
        username.sendKeys(new String(readProps.getParticipantEmail()));
        username.submit();
        waitForPassword();
    }

    public void enterGmailPassword() {
        password = driver.findElement(By.cssSelector("input[id='Passwd']"));
        password.sendKeys(new String(readProps.getParticipantEmailPwd()));
        password.submit();
        waitForInbox();
    }

    public void waitForGmail() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.titleIs("Gmail")
        );
    }

    public void waitForPassword() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='Passwd']"))
        );
    }

    public void waitForInbox() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(
                ExpectedConditions.titleContains("Inbox")
        );
    }


}
