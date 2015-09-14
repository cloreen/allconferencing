package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 7/20/2015.
 */
public class AddDocPageObject extends BaseSeleniumTest {
    private static WebDriver driver = getDriver();
    private ReadPropertyFile readProps = null;
    private String savedDocName = "";

    private WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public AddDocPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get() {

        driver.get(readProps.getUrl());

    }


    public void startNewDoc() {
        // Opens new document in Meeting Controller)
        WebElement meetingControlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li"));

        Actions action = new Actions(driver);
        action.moveToElement(meetingControlsMenu).click().moveByOffset(0, 96).clickAndHold().perform();
        action.release().perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addContent(String content) {
        new Actions(driver).sendKeys(new String(content)).perform();
    }

    // Saves the document with a Name and Description and adds to current doc group, checks doc has proper name.
    public void saveAndCloseDocument(String name, String description) {
        WebElement saveButton = driver.findElement(By.xpath("//*[contains(text(), 'Save')]"));
        saveButton.click();

        WebElement saveDocumentName = driver.findElement(By.cssSelector("#newdocname"));
        saveDocumentName.sendKeys(new String(name));

        WebElement saveDocumentDesc = driver.findElement(By.cssSelector("#newdocdesc"));
        saveDocumentDesc.sendKeys(new String(description));

        WebElement addinDocGrpCheckbox = driver.findElement(By.cssSelector("#addinrg"));
        addinDocGrpCheckbox.click();

        WebElement saveDocumentSaveButton = driver.findElement(By.cssSelector("#savenewdocbutton-button"));
        saveDocumentSaveButton.click();

        // Wait for doc to save
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check for proper name. If name already exists, assertionError will result. Confirm popup and save
        // with assigned name. Then close document.
        try {
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(name));
            WebElement closeDocumentButton = driver.findElement(By.xpath("/html/body/div[6]/div/a"));
            String js = "arguments[0].style.visibility='visible';";
            ((JavascriptExecutor) driver).executeScript(js, closeDocumentButton);
            closeDocumentButton.click();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String js1 = "arguments[0].style.visibility='hidden';";
            ((JavascriptExecutor) driver).executeScript(js1, closeDocumentButton);
        } catch (AssertionError ae) {
            //Document name already exists. Confirm error message and copy suggested document name.
            WebElement nameErrorOkButton = driver.findElement(By.cssSelector("#dupwarnok-button"));
            nameErrorOkButton.click();
            savedDocName = saveDocumentName.getAttribute("value");
            System.out.println("Saved Document Name is: " + savedDocName);
            saveDocumentSaveButton.click();
            // Wait for save to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(savedDocName));
            //WebElement closeDocumentButton = driver.findElement(By.xpath("//div[contains(.,'new_test_doc')]"));
            WebElement closeDocumentButton = driver.findElement(By.xpath("/html/body/div[6]/div/a"));
            String js = "arguments[0].style.visibility='visible';";
            ((JavascriptExecutor) driver).executeScript(js, closeDocumentButton);
            closeDocumentButton.click();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String js1 = "arguments[0].style.visibility='hidden';";
            ((JavascriptExecutor) driver).executeScript(js1, closeDocumentButton);
        } catch (Exception e) {
            //Document name already exists. Confirm error message and copy suggested document name.
            WebElement nameErrorOkButton = driver.findElement(By.cssSelector("#dupwarnok-button"));
            nameErrorOkButton.click();
            savedDocName = saveDocumentName.getAttribute("value");
            System.out.println("Saved Dcoument Name is: " + savedDocName);
            saveDocumentSaveButton.click();
            // Wait for save to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(savedDocName));
            //WebElement closeDocumentButton = driver.findElement(By.xpath("//div[contains(.,'new_test_doc')]/a"));
            WebElement closeDocumentButton = driver.findElement(By.xpath("/html/body/div[6]/div/a"));
            String js = "arguments[0].style.visibility='visible';";
            ((JavascriptExecutor) driver).executeScript(js, closeDocumentButton);
            closeDocumentButton.click();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String js1 = "arguments[0].style.visibility='hidden';";
            ((JavascriptExecutor) driver).executeScript(js1, closeDocumentButton);
        }
    }

    // Open document in editor.
    public void editDocument() {
        WebElement editDocButton = driver.findElement(By.xpath(".//img[@title='Edit']"));
        editDocButton.click();
    }
}
