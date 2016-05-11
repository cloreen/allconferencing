package com.lotus.allconferencing;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ben on 4/30/2016.
 */
public class Utility {

    public static void captureScreenshot(WebDriver driverName, String fileName) {
        WebDriver driver = driverName;
        TakesScreenshot screen = (TakesScreenshot)driver;
        File src = screen.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, new File("./Screenshots/" + fileName + ".png"));
        } catch (IOException e) {
            System.out.println("Screenshot capture failed!");
            e.printStackTrace();
        }

    }
}
