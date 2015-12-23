package com.lotus.allconferencing.meeting_controller.tests;

import com.lotus.allconferencing.BaseSeleniumTest;
import com.lotus.allconferencing.meeting_controller.pages.AddDocPageObject;
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
    private String documentDescription = "New Test Doc Description";
    private String documentContent = "This is test content";

    public static WebDriver getBrowser(String browserType) {
        if(driver == null) {
            if(browserType.equals("Firefox")) {
                driver = new FirefoxDriver();
            } else if (browserType.equals("Chrome")) {
            }
        }
        return driver;
    }

    public AddDocPageObject addDocPage = new AddDocPageObject(driver);

    @Test
    public void addNewDocument() {
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        htmlElement.click();

        addDocPage.startNewDoc();
        addDocPage.addContent(documentContent);
        addDocPage.saveAndCloseDocument(documentName, documentDescription);
        addDocPage.editDocument();

        WebElement newDocTitleBar = driver.findElement(By.xpath("/html/body/div/div/div"));
        assert(newDocTitleBar.getText().contains("Edit File - " + documentName + ".html"));
    }
}
