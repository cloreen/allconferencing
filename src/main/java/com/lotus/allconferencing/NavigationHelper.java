package com.lotus.allconferencing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Ben on 4/25/2015.
 */
public abstract class NavigationHelper extends Thread {
    public WebDriver webDriver;

    public int getNumberOfElementsFound(By by) {
        return webDriver.findElements(by).size();
    }

    public WebElement getElementWithIndex(By by, int pos) {
        return webDriver.findElements(by).get(pos);
    }
}
