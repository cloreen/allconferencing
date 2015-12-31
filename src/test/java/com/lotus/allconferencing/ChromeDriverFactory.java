package com.lotus.allconferencing;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ChromeDriverFactory
{
    private static String chromeDriverRepository = "http://chromedriver.storage.googleapis.com/";

    public static WebDriver getChromeDriver() throws MalformedURLException,
            IOException, ZipException
    {
        String chromeDriverFileName = "chromedriver.exe";
        File chromeDriverFile = new File(chromeDriverFileName);

        if (!chromeDriverFile.exists())
        {
            installChromeDriver();
        }

        setChromeDriverProperty(chromeDriverFileName);

        return new ChromeDriver();
    }

    private static void setChromeDriverProperty(String chromeDriverFileName)
    {
        System.setProperty("webdriver.chrome.driver", chromeDriverFileName);
    }

    private static void installChromeDriver() throws IOException,
            MalformedURLException, ZipException
    {
        String newestVersion = getNewestVersion();
        String targetFile = "chromedriver_win32.zip";
        String downloadUrl = chromeDriverRepository + newestVersion + "/"
                + targetFile;
        String downloadFileName = FilenameUtils.getName(downloadUrl);
        File downloadFile = new File(downloadFileName);
        String projectRootDirectory = System.getProperty("user.dir");

        FileUtils.copyURLToFile(new URL(downloadUrl), downloadFile);

        ZipFile zipFile = new ZipFile(downloadFile);
        zipFile.extractAll(projectRootDirectory);
        FileUtils.deleteQuietly(downloadFile);
    }

    private static String getNewestVersion() throws MalformedURLException,
            IOException
    {
        String newestVersionUrl = chromeDriverRepository + "LATEST_RELEASE";
        InputStream input = new URL(newestVersionUrl).openStream();

        return IOUtils.toString(input);
    }
}