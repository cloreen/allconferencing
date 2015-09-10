package com.lotus.allconferencing.meeting_controller;

import com.lotus.allconferencing.BaseSeleniumTest;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.lang.reflect.Array;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ben on 9/1/2015.
 */
public class AddDocumentTest extends BaseSeleniumTest {
    private static WebDriver driver = getDriver();
    private String documentName = "new_test_doc";

    public static WebDriver getBrowser(String browserType) {
        if(driver == null) {
            if(browserType.equals("Firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("Chrome")) {
            }
        }
        return driver;
    }

    @Test
    public void addNewDocument() {
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();
        WebElement meetingControlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li"));

        Actions action = new Actions(driver);
        action.moveToElement(meetingControlsMenu).click().moveByOffset(0, 96).clickAndHold().perform();
        action.release().perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Actions(driver).sendKeys(new String("This is test content")).perform();
        /*
        WebElement docInput = driver.findElement(By.tagName("textarea"));
        String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
        ((JavascriptExecutor) driver).executeScript(js, docInput);
        docInput.click();
        docInput.sendKeys(new String("This is test content"));
        */

        WebElement saveButton = driver.findElement(By.xpath("//*[contains(text(), 'Save')]"));
        saveButton.click();

        WebElement saveDocumentName = driver.findElement(By.cssSelector("#newdocname"));
        saveDocumentName.sendKeys(documentName);

        WebElement saveDocumentDesc = driver.findElement(By.cssSelector("#newdocdesc"));
        saveDocumentDesc.sendKeys(new String("New Test Doc Description"));

        WebElement addinDocGrpCheckbox = driver.findElement(By.cssSelector("#addinrg"));
        addinDocGrpCheckbox.click();

        WebElement saveDocumentCancelButton = driver.findElement(By.cssSelector("#cancelsavebutton-button"));
        WebElement saveDocumentSaveButton = driver.findElement(By.cssSelector("#savenewdocbutton-button"));
        saveDocumentSaveButton.click();
        // Wait for save to process
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(documentName));
            WebElement closeDocumentButton = driver.findElement(By.className("container-close"));
            closeDocumentButton.click();
            WebElement editDocButton = driver.findElement(By.xpath(".//img[@title='Edit']"));
            editDocButton.click();
            /*
            WebElement editColumn = driver.findElement(By.xpath(".//*[@title='Edit File']"));
            Actions clickEditButton = new Actions(driver);
            clickEditButton.moveToElement(editColumn).click().moveByOffset(0, 46).doubleClick().perform();
            */
            //WebElement editFileTitleBar = driver.findElement(By.cssSelector("#new_doc_panel_1441431617318_h"));
        } catch (AssertionError ae) {
            //Document name already exists. Confirm error message and copy suggested document name.
            WebElement nameErrorOkButton = driver.findElement(By.cssSelector("#dupwarnok-button"));
            nameErrorOkButton.click();
            documentName = saveDocumentName.getAttribute("value");
            System.out.println("Saved Dcoument Name is: " + documentName);
            saveDocumentSaveButton.click();
            // Wait for save to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(documentName));
            //WebElement closeDocumentButton = driver.findElement(By.xpath("//div[contains(.,'new_test_doc')]"));
            WebElement closeDocumentButton = driver.findElement(By.xpath("/html/body/div[6]/div/a"));
            /*
            List<WebElement> closeButtonList = driver.findElements(By.cssSelector(".container-close"));
            for (int i = 0; i < closeButtonList.size(); i++) {
                WebElement closeButton = closeButtonList.get(i);
                String js = "arguments[0].style.visibility='visible';";
                ((JavascriptExecutor) driver).executeScript(js, closeButton);
                closeButton.click();
            }
            */
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
            WebElement editDocButton = driver.findElement(By.xpath(".//img[@title='Edit']"));
            editDocButton.click();
            /*
            WebElement editColumn = driver.findElement(By.xpath(".//*[@title='Edit File']"));
            Actions clickEditButton = new Actions(driver);
            clickEditButton.moveToElement(editColumn).click().moveByOffset(0, 46).doubleClick().perform();
            */
        } catch (Exception e) {
            //Document name already exists. Confirm error message and copy suggested document name.
            WebElement nameErrorOkButton = driver.findElement(By.cssSelector("#dupwarnok-button"));
            nameErrorOkButton.click();
            documentName = saveDocumentName.getAttribute("value");
            System.out.println("Saved Dcoument Name is: " + documentName);
            saveDocumentSaveButton.click();
            // Wait for save to process
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div[6]/div/div"));
            assertThat("Doc has been saved with intended name", newDocTitleBar.getText().contains(documentName));
            //WebElement closeDocumentButton = driver.findElement(By.xpath("//div[contains(.,'new_test_doc')]/a"));
            /*
            List<WebElement> closeButtonList = driver.findElements(By.cssSelector(".container-close"));
            for (int i = 0; i < closeButtonList.size(); i++) {
                WebElement closeButton = closeButtonList.get(i);
                String js = "arguments[0].style.visibility='visible';";
                ((JavascriptExecutor) driver).executeScript(js, closeButton);
                closeButton.click();
            }
            //WebElement closeDocumentButton = driver.findElement(By.xpath("//div[contains(.,'new_test_doc')]"));
            */
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
            WebElement editDocButton = driver.findElement(By.xpath(".//img[@title='Edit']"));
            editDocButton.click();
            /*
            WebElement editColumn = driver.findElement(By.xpath(".//*[@title='Edit File']"));
            Actions clickEditButton = new Actions(driver);
            clickEditButton.moveToElement(editColumn).click().moveByOffset(0, 46).doubleClick().perform();
            */
        }



        WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div/div/div"));
        assert(newDocTitleBar.getText().contains("Edit File - " + documentName + ".html"));

    }
}
