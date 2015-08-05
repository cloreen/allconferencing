package com.lotus.allconferencing.webdriver.manager;

//import com.opera.core.systems.OperaDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A singleton style manager to maintain Drivers to prevent
 * test slowdown for creating a browser for each class with tests.
 *
 * Also counts time to start a browser and extrapolates from that how much
 * time you have saved using such hacky code.
 */
public class Driver extends Thread {
    private static WebDriver aDriver=null;
    private static long browserStartTime = 0L;
    private static long savedTimecount = 0L;
    public static final long DEFAULT_TIMEOUT_SECONDS = 10;
    private static boolean avoidRecursiveCall=false;
    public static final String BROWSER_PROPERTY_NAME = "selenium2basics.webdriver";

    private static final String DEFAULT_BROWSER = "FIREFOX";

    public enum BrowserName{FIREFOX, GOOGLECHROME, SAUCELABS, OPERA, IE, HTMLUNIT}

    public static BrowserName currentDriver;

    private static BrowserName useThisDriver = null;

    // default for browsermob localhost:8080
    // default for fiddler: localhost:8888
    public static String PROXYHOST="localhost";
    public static String PROXYPORT="8080";
    public static String PROXY=PROXYHOST+":"+PROXYPORT;

    public static WebDriver set(BrowserName aBrowser){
        useThisDriver = aBrowser;

        // close any existing driver
        if(aDriver != null){
            aDriver.quit();
            aDriver = null;
        }
        return null;
    }

    public static WebDriver get() {

        if(useThisDriver == null){

            String defaultBrowser = System.getProperty(BROWSER_PROPERTY_NAME, DEFAULT_BROWSER);

            /* Note:
                    I generally use the Java 1.7 format that you can see below this chunk
                    of code. where the switch statement uses a String.

                    Java 1.6 does not support this, since people have tried to use this code
                    on older versions of Java, and this is really the only Java 1.7 specific
                    functionality that I use. I have converted this code to Java 1.6 with a
                    series of if statements to replicate the switch statement.

                    I could have used an enum, or refactored it to use objects
                    but I wanted to keep the code simple.

                    Hence the reason this code is different from what you might see in the
                    lecture. But I have left this comment in to explain the situation.
             */
            if("FIREFOX".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.FIREFOX;

            if("CHROME".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.GOOGLECHROME;

            if("IE".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.IE;

            if("OPERA".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.OPERA;

            if("SAUCELABS".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.SAUCELABS;

            if("HTMLUNIT".contentEquals(defaultBrowser))
                useThisDriver = BrowserName.HTMLUNIT;

            // if none of the previous if statements were exercised then useThisDriver will
            // still be none, this is the 'default' line from the Switch statement.
            if(useThisDriver==null)
                throw new RuntimeException("Unknown Browser in " + BROWSER_PROPERTY_NAME + ": " + defaultBrowser);

            /* Code below requires Java 1.7 to switch on String
            *  This would be my default way of writing the code, but have
            *  not made it the default in this code base because people often compile
            *  against 1.6 settings.
            */

            switch (DEFAULT_BROWSER){
                case "FIREFOX":
                    useThisDriver = BrowserName.FIREFOX;
                    break;
                case "CHROME":
                    useThisDriver = BrowserName.GOOGLECHROME;
                    break;
                case "IE":
                    useThisDriver = BrowserName.IE;
                    break;
                case "OPERA":
                    useThisDriver = BrowserName.OPERA;
                    break;
                case "SAUCELABS":
                    useThisDriver = BrowserName.SAUCELABS;
                    break;
                case "HTMLUNIT":
                    useThisDriver = BrowserName.HTMLUNIT;
                    break;
                default:
                    throw new RuntimeException("Unknown Browser in " + BROWSER_PROPERTY_NAME + ": " + defaultBrowser);
            }


        }


        if(aDriver==null){


            long startBrowserTime = System.currentTimeMillis();

            switch (useThisDriver) {
                case FIREFOX:
                    FirefoxProfile profile = new FirefoxProfile();
                    profile.setEnableNativeEvents(true);

                    aDriver = new FirefoxDriver();//profile);
                    currentDriver = BrowserName.FIREFOX;
                    break;

                case OPERA:

                    // OperaDriver only supports version 12.x
                    // https://code.google.com/p/selenium/wiki/OperaDriver
                    aDriver = new OperaDriver();
                    currentDriver = BrowserName.OPERA;
                    break;

                case HTMLUNIT:

                    aDriver = new HtmlUnitDriver(true);
                    currentDriver = BrowserName.HTMLUNIT;
                    break;

                case IE:

                    setDriverPropertyIfNecessary("webdriver.ie.driver", "/../tools/iedriver_64/IEDriverServer.exe", "C://webdrivers/iedriver_64/IEDriverServer.exe");

                    aDriver = new InternetExplorerDriver();
                    currentDriver = BrowserName.IE;
                    break;

                case GOOGLECHROME:

                    aDriver = new ChromeDriver();
                    //setDriverPropertyIfNecessary("webdriver.chrome.driver","/../tools/chromedriver/chromedriver.exe","C://webdrivers/chromedriver/chromedriver.exe");

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("disable-plugins");
                    options.addArguments("disable-extensions");
                    // with Chrome v35 it now reports an error on --ignore-certificate-errors
                    // so call with args "test-type"
                    // https://code.google.com/p/chromedriver/issues/detail?id=799
                    options.addArguments("test-type");

                    aDriver = new ChromeDriver(options);
                    currentDriver = BrowserName.GOOGLECHROME;
                    break;

                case SAUCELABS:

                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability("version", "5");
                    capabilities.setCapability("platform", Platform.XP);
                    try {
                        // add url to environment variables to avoid releasing with source
                        String sauceURL = System.getenv("SAUCELABS_URL");
                        aDriver = new RemoteWebDriver(
                                new URL(sauceURL),
                                capabilities);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    currentDriver = BrowserName.SAUCELABS;
                    break;
            }


            long browserStartedTime = System.currentTimeMillis();
            browserStartTime = browserStartedTime - startBrowserTime;

            // we want to shutdown the shared brower when the tests finish
            Runtime.getRuntime().addShutdownHook(
                    new Thread(){
                        public void run(){
                            Driver.quit();
                        }
                    }
            );

        }else{

            try{
                // is browser still alive
                if(aDriver.getWindowHandle()!=null){
                    // assume it is still alive
                }
            }catch(Exception e){
                if(avoidRecursiveCall){
                    // something has gone wrong as we have been here already
                    throw new RuntimeException();
                }

                quit();
                aDriver=null;
                avoidRecursiveCall = true;
                return get();
            }

            savedTimecount += browserStartTime;
            System.out.println("Saved another " + browserStartTime + "ms : total saved " + savedTimecount + "ms");
        }

        avoidRecursiveCall = false;
        return aDriver;
    }

    private static void setDriverPropertyIfNecessary(String propertyKey, String relativeToUserPath, String absolutePath) {
        // http://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html

        if(!System.getProperties().containsKey(propertyKey)){

            String currentDir = System.getProperty("user.dir");
            String chromeDriverLocation = currentDir + relativeToUserPath;
            File driverExe = new File(chromeDriverLocation);
            if(driverExe.exists()){
                System.setProperty(propertyKey, chromeDriverLocation);
            }else{
                driverExe = new File(absolutePath);
                if(driverExe.exists()){
                    System.setProperty(propertyKey, absolutePath);
                }else{
                    // expect an error on the follow through when we try to use the driver
                }
            }
        }
    }

    public static WebDriver get(String aURL, boolean maximize){
        get();
        aDriver.get(aURL);
        if(maximize){
            try{
                aDriver.manage().window().maximize();
            }catch(UnsupportedCommandException e){
                System.out.println("Remote Driver does not support maximise");
            }catch(UnsupportedOperationException e){
                System.out.println("Opera driver does not support maximize yet");
            }
        }
        return aDriver;
    }

    public static WebDriver get(String aURL){
        return get(aURL,true);
    }

    public static void quit(){
        if(aDriver!=null){
            System.out.println("total time saved by reusing browsers " + savedTimecount + "ms");
            try{
                aDriver.quit();
                aDriver=null;
            }catch(Exception e){
                // I don't care about errors at this point
            }

        }
    }
}
