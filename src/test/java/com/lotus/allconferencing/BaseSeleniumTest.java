package com.lotus.allconferencing;

import net.lingala.zip4j.exception.ZipException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Ben on 9/3/2015.
 */

/***********************************************************************************************************************
 * TODO - Add Columns to Excel Test Data sheet (ENVIRONMENT, PLATFORM)
 * TODO - Modify Excel method logic to properly pass data
 * TODO - Add ENUM "ENVIRONMENT" (LOCAL, GRID, SAUCELABS, ?)
 * TODO - Add ENUM "PLATFORM" (WINXP, WINVISTA, WIN7, WIN8_1, WIN10, MAC, YOSEMITE, ?)
 * TODO - Modify ExcelData class to properly handle new options
 * TODO - Modify OldScheduler_v2a_Invite_Test classes to properly use new test data arguments
 ***********************************************************************************************************************/
public abstract class BaseSeleniumTest {
    private static WebDriver driver;
    private static RemoteWebDriver remoteDriver;
    private static ReadPropertyFile readProps = null;
    private static Integer minWindowWidth = 1365;
    private static Integer minWindowHeight = 768;
    private static String gridHubPort = null;
    private static String gridHubPID = null;
    private static String gridURL = "http://localhost:4444/wd/hub";
    private static Boolean testInParallel = false;

    private static ChromeDriverFactory chromeDriverFactory = new ChromeDriverFactory();


    /*******************************************************************************************************************
     * Here we define the type of driver as well as its properties
     ******************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------

    public enum BrowserName {FIREFOX, CHROME, IE, OPERA, SAFARI, SAUCELABS, HTMLUNIT, PHANTOMJS, GRID}

    public static WebDriver setDriver(BrowserName browser, Boolean multiDriver) {
        testInParallel = multiDriver;


        switch (browser) {
            case FIREFOX:
                FirefoxBinary ffbinary = new FirefoxBinary(new File("D:\\testing\\browsers\\firefox\\releases\\win32\\42_0\\firefox.exe"));
                FirefoxProfile ffprofile = new FirefoxProfile();
                driver = new FirefoxDriver(ffbinary, ffprofile);
                /*
                if (testInParallel = false) {
                    //System.setProperty("webdriver.gecko.driver", "D:\\testing\\browsers\\geckodriver\\releases\\win32\\0_14_0\\geckodriver.exe");
                    FirefoxBinary ffbinary = new FirefoxBinary(new File("D:\\testing\\browsers\\firefox\\releases\\win32\\46_0\\firefox.exe"));
                    FirefoxProfile ffprofile = new FirefoxProfile();
                    driver = new FirefoxDriver(ffprofile);
                } else {
                    System.setProperty("webdriver.gecko.driver", "D:\\testing\\browsers\\geckodriver\\releases\\win32\\0_14_0\\geckodriver.exe");
                    System.setProperty("webdriver.gecko.port", "4445");
                    FirefoxProfile ffprofile = new FirefoxProfile();
                    driver = new FirefoxDriver(ffprofile);
                }
                */
                break;

            case CHROME:
//                setDriverPropertyIfNecessary("webdriver.chrome.driver", "../chromedriver.exe", "C:\\IntelliJ_Projects\\allconferencing\\chromedriver.exe");
//                driver = new ChromeDriver();
                try {
                    driver = chromeDriverFactory.getChromeDriver("C:\\IntelliJ_Projects\\allconferencing\\chromedriver.exe");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ZipException e) {
                    e.printStackTrace();
                }
                break;

            case IE:
                setDriverPropertyIfNecessary("webdriver.ie.driver", "/../tools/iedriver_32/IEDriverServer.exe", "C://Users/Ben/Downloads/IEDriverServer_Win32_2.53.0/IEDriverServer.exe");

                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ieCapabilities.setCapability("requireWindowFocus", true);
                ieCapabilities.setCapability("ie.ensureCleanSession", true);
                ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
                ieCapabilities.setJavascriptEnabled(true);

                driver = new InternetExplorerDriver(ieCapabilities);
                break;

            case OPERA:
                setDriverPropertyIfNecessary("webdriver.opera.driver", "/../tools/operadriver_32/operadriver.exe", "C://Users/Ben/Downloads/operadriver_win32/operadriver.exe");
                driver = new OperaDriver();
                break;

            case SAFARI:
                DesiredCapabilities safariCapabilities = DesiredCapabilities.safari();
                safariCapabilities.setCapability("version", "8.0.3");
                safariCapabilities.setCapability("platform", Platform.MAC);
                safariCapabilities.setCapability("os_version", Platform.YOSEMITE);

                safariCapabilities.setCapability("ensureCleanSession", true);
                driver = new SafariDriver();
                break;

            case SAUCELABS:
                DesiredCapabilities sauceCapabilities = DesiredCapabilities.firefox();
                sauceCapabilities.setCapability("version", "5");
                sauceCapabilities.setCapability("platform", Platform.XP);
                try {
                    // add url to environment variables to avoid releasing with source
                    String sauceURL = System.getenv("SAUCE_URL");
                    driver = new RemoteWebDriver(
                            new URL(sauceURL),
                            sauceCapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case HTMLUNIT:
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setJavascriptEnabled(false);
                desiredCapabilities.setBrowserName("FIREFOX");
                desiredCapabilities.setVersion("38");
                driver = new HtmlUnitDriver(desiredCapabilities);
                break;

            case PHANTOMJS:
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setJavascriptEnabled(true);
                caps.setCapability("handlesAlerts", true);
                caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Software\\Phantom\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
                driver = new PhantomJSDriver(caps);
//                driver.manage().window().maximize();
                Dimension windowSize = driver.manage().window().getSize();
                driver.manage().window().setSize(setWindowSize(windowSize));
                break;

            case GRID:
                // Start Grid Hub
                List hubCmdAndArgs = Arrays.asList("cmd.exe", "/c", "starthub.bat");
                File hubCmdDir = new File("C:\\IntelliJ_Projects\\allconferencing\\src\\test\\resources");

                ProcessBuilder hubProcessBuilder = new ProcessBuilder(hubCmdAndArgs);
                hubProcessBuilder.directory(hubCmdDir);
                try {
                    Process hubProcess = hubProcessBuilder.start();
                    System.out.println("The hub should have started.");
                    Thread.sleep(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Start Node Hub
                List nodeCmdAndArgs = Arrays.asList("cmd.exe", "/c", "startnode.bat");
                File nodeCmdDir = new File("C:\\IntelliJ_Projects\\allconferencing\\src\\test\\resources");

                ProcessBuilder nodeProcessBuilder = new ProcessBuilder(nodeCmdAndArgs);
                nodeProcessBuilder.directory(nodeCmdDir);
                try{
                    Process nodeProcess = nodeProcessBuilder.start();
                    Thread.sleep(5000);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Setup driver
                FirefoxBinary ffGridBinary = new FirefoxBinary(new File("D:\\testing\\browsers\\firefox\\releases\\win32\\46_0\\firefox.exe"));
                FirefoxProfile ffGridProfile = new FirefoxProfile();
//                System.setProperty("webdriver.firefox.bin", "D:\\testing\\browsers\\firefox\\releases\\win32\\46_0\\firefox.exe");
                DesiredCapabilities gridCaps = new DesiredCapabilities();
                gridCaps.setBrowserName("firefox");
//                gridCaps.setVersion("35");
                gridCaps.setPlatform(Platform.WIN8_1);
                gridCaps.setCapability(FirefoxDriver.BINARY, "D:\\testing\\browsers\\firefox\\releases\\win32\\46_0\\firefox.exe");
                try {
                    // add url to environment variables to avoid releasing with source
//                    String gridURL = System.getenv("GRID_URL");
                    driver = new RemoteWebDriver(new URL(gridURL), gridCaps);
                    if(testInParallel==true)
                        setDriverForParallelExec((RemoteWebDriver) driver);
                } catch (MalformedURLException e) {
                    System.out.println("The malformed URL is: " + gridURL);
                    e.printStackTrace();
                } /*finally {
                    Boolean isHubActive = true;
                    Boolean isNodeActive = true;
                    while (isNodeActive != false) {
                        isNodeActive = getNodeStatus();
                        while(isNodeActive) {
                            shutDownNode();
                            isNodeActive = getNodeStatus();
                        }
                        if(isNodeActive == false)
                            System.out.println("Node has been shut down!");
                    }
                    while (isHubActive != false) {
                        isHubActive = getHubStatus();
                        while(isHubActive) {
                            shutDownHub();
                            isHubActive = getHubStatus();
                        }
                        if(isHubActive == false)
                            System.out.println("Hub has been shut down!");
                    }
                }*/


        }
        return driver;
    }
    //==================================================================================================================



    /*******************************************************************************************************************
     * Helper methods for setting the driver
     ******************************************************************************************************************/
     //-----------------------------------------------------------------------------------------------------------------
    private static void setDriverPropertyIfNecessary(String propertyKey, String relativeToUserPath, String absolutePath) {
        // http://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html

        if(!System.getProperties().containsKey(propertyKey)){

            String currentDir = System.getProperty("user.dir");
            String driverLocation = currentDir + relativeToUserPath;
            File driverExe = new File(driverLocation);
            if(driverExe.exists()){
                System.setProperty(propertyKey, driverLocation);
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

    public static WebDriver getDriver() {

        if(driver != null) {
            return driver;
        }

        return new FirefoxDriver();
    }
    //==================================================================================================================


    /*******************************************************************************************************************
    * Methods to manage hub and nodes
     *******************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------
    private static void shutDownHub() {

        List hubShutDownCmdAndArgs = Arrays.asList("cmd.exe", "/c", "TASKKILL /F /PID " + gridHubPID);
//        File hubShutDownCmdDir = new File("C:\\IntelliJ_Projects\\allconferencing\\src\\test\\resources");
        ProcessBuilder hubShutDownProcessBuilder = new ProcessBuilder(hubShutDownCmdAndArgs);
//        hubShutDownProcessBuilder.directory(hubShutDownCmdDir);
        try {
            Process hubShutDownProcess = hubShutDownProcessBuilder.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void shutDownNode() {
        WebDriver tempDriver;
        tempDriver = new FirefoxDriver();
        tempDriver.get("http://localhost:5555/selenium-server/driver/?cmd=shutDownSeleniumServer");
        tempDriver.quit();
    }

    private static Boolean getNodeStatus() {
        // Check for status of node
        List nodeStatusCmdAndArgs = Arrays.asList("cmd.exe", "/c", "node_status.bat");
        File nodeStatusCmdDir = new File("C:\\IntelliJ_Projects\\allconferencing\\src\\test\\resources");
        ProcessBuilder nodeStatusProcessBuilder = new ProcessBuilder(nodeStatusCmdAndArgs);
        nodeStatusProcessBuilder.directory(nodeStatusCmdDir);
        try{
            Process nodeStatusProcess = nodeStatusProcessBuilder.start();
            Thread.sleep(2000);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check output of netstat for hub process
        String nodeStatusFile = "C:\\temp\\netstat_node.txt";
        String line = null;
        Boolean isActive = false;
        try {
            FileReader reader = new FileReader(nodeStatusFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            int index = 0;
            while ((index != 1) && ((line = bufferedReader.readLine()) != null)) {
                System.out.println(line);
                String[] lineFrags = line.split("\\s+");
                for (String fragment : lineFrags) {
                    if (fragment.contains("5555")) {
                        System.out.println("Node is active!");
                        isActive = true;
                        break;
                    }
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isActive;
    }

    private static Boolean getHubStatus() {
        // Check for status of hub
        List hubStatusCmdAndArgs = Arrays.asList("cmd.exe", "/c", "hub_status.bat");
        File hubStatusCmdDir = new File("C:\\IntelliJ_Projects\\allconferencing\\src\\test\\resources");
        ProcessBuilder hubStatusProcessBuilder = new ProcessBuilder(hubStatusCmdAndArgs);
        hubStatusProcessBuilder.directory(hubStatusCmdDir);
        try{
            Process hubStatusProcess = hubStatusProcessBuilder.start();
            Thread.sleep(2000);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check output of netstat for hub process
        String hubStatusFile = "C:\\temp\\netstat_hub.txt";
        String line = null;
        Boolean isActive = false;
        try {
            FileReader reader = new FileReader(hubStatusFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            int index = 0;
            while ((index != 1) && ((line = bufferedReader.readLine()) != null)) {
                System.out.println(line);
                String[] lineFrags = line.split("\\s+");
                for (String fragment : lineFrags) {
                    if (isActive == true) {
                        gridHubPID = lineFrags[lineFrags.length-1];
                        System.out.println("The PID of the hub is: " + gridHubPID);
                    }
                    if (fragment.contains("4444")) {
                        System.out.println("Hub is active!");
                        isActive = true;
                        gridHubPort = fragment;
                    }
                    if (gridHubPID != null && gridHubPort != null) {
                        break;
                    }
                }
                index++;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isActive;
    }
    //==================================================================================================================

    /*******************************************************************************************************************
     * For running Grid tests in parallel
     * ****************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------
    public static ThreadLocal<RemoteWebDriver> remoteWebDriverThreadLocal = new ThreadLocal<RemoteWebDriver>();
    public static void setDriverForParallelExec(RemoteWebDriver remoteDriver) {
        remoteWebDriverThreadLocal.set(remoteDriver);
    }

    /*******************************************************************************************************************
     * Browser window helper methods
     * ****************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------

    private static Dimension setWindowSize(Dimension windowDimension) {
        int newWindowWidth = Math.max(windowDimension.width, minWindowWidth);
        int newWindowHeight = Math.max(windowDimension.height, minWindowHeight);
        Dimension newWindowDimension = new Dimension(newWindowWidth, newWindowHeight);
        return newWindowDimension;
    }
    //==================================================================================================================


    /*******************************************************************************************************************
     * Initialize the properties file for reading test data from it
     * ****************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------

    public static ReadPropertyFile getGridSettings() {
        try {
            readProps = new ReadPropertyFile("GRID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProps;
    }

    public static ReadPropertyFile getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProps;
    }

    //==================================================================================================================


    /*******************************************************************************************************************
     * Gets test data from Excel spreadsheet
     * ****************************************************************************************************************/
    //------------------------------------------------------------------------------------------------------------------

    @Parameterized.Parameters
    public static Collection dataFromExcel(Integer rowNum) throws IOException {
        InputStream spreadsheet = new FileInputStream("src/test/resources/allconftest_data.xlsx");
        return new SpreadsheetData(spreadsheet, rowNum).getData();
    }

    public static class SpreadsheetData {

        private transient Collection<Object[]> data = null;

        public SpreadsheetData(final InputStream excelInputStream, Integer rowNum) throws IOException {
            this.data = loadFromSpreadsheet(excelInputStream, rowNum);
        }

        public Collection<Object[]> getData() {
            System.out.println("Value of 'data' in getData(): " + data);
            return data;
        }

        private Collection<Object[]> loadFromSpreadsheet(final InputStream excelFile, Integer rowNum) throws IOException {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

            data = new ArrayList<Object[]>();
            XSSFSheet sheet = workbook.getSheetAt(2);

            int numberOfColumns = countNonEmptyColumns(sheet);
            List<Object[]> rows = new ArrayList<>();
            List<Object> rowData = new ArrayList<>();

            // Setup new for loop based on int, then assign sheet.getRow(int) to row (see Bas' example - On Test Automation)
            if (rowNum == 0) {
                for(int i = 0; i <= sheet.getLastRowNum(); i++) {
//                i++;
                    int lastRowNum = sheet.getLastRowNum();
                    System.out.println("Last row is: " + lastRowNum);
                    Row row = sheet.getRow(i);
                    rowData.clear();
                    for (int column = 1; column < numberOfColumns; column++) {
                        Cell cell = row.getCell(column);
                        rowData.add(objectFrom(cell));
                    }
                    rows.add(rowData.toArray());
                }
            } else {
                Row row = sheet.getRow(rowNum);
                rowData.clear();
                for (int column = 1; column < numberOfColumns; column++) {
                    Cell cell = row.getCell(column);
                    rowData.add(objectFrom(cell));
                }
                rows.add(rowData.toArray());
            }
            return rows;
        }


        private boolean isEmpty(final Row row) {
            Cell firstCell = row.getCell(0);
            boolean rowIsEmpty = (firstCell == null)
                    || (firstCell.getCellType() == Cell.CELL_TYPE_BLANK);
            return rowIsEmpty;
        }

        /**
         * Count the number of columns, using the number of non-empty cells in the
         * first row.
         */
        private int countNonEmptyColumns(final XSSFSheet sheet) {
            Row firstRow = sheet.getRow(1);
            return firstEmptyCellPosition(firstRow);
        }

        private int firstEmptyCellPosition(final Row cells) {
            int columnCount = 0;
            for (Cell cell : cells) {
                if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    break;
                }
                columnCount++;
            }
            return columnCount;
        }

        private Object objectFrom(final Cell cell) {
            Object cellValue = null;
            Double cellDoubleValue = null;
            Integer cellIntegerValue = null;
            try {
                cellValue = cell.getRichStringCellValue().getString();
            } catch (IllegalStateException ise) {
                cellDoubleValue = cell.getNumericCellValue();
                cellIntegerValue = cellDoubleValue.intValue();
                cellValue = cellIntegerValue.toString();
            }

            System.out.println(cellValue);
            return cellValue;
        }

        private Object getNumericCellValue(final Cell cell) {
            Object cellValue;
            if (DateUtil.isCellDateFormatted(cell)) {
                cellValue = new Date(cell.getDateCellValue().getTime());
            } else {
                cellValue = cell.getNumericCellValue();
            }
            return cellValue;
        }

        private Object evaluateCellFormula(final XSSFWorkbook workbook, final Cell cell) {
            FormulaEvaluator evaluator = workbook.getCreationHelper()
                    .createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            Object result = null;

            if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                result = cellValue.getBooleanValue();
            } else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                result = cellValue.getNumberValue();
            } else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
                result = cellValue.getStringValue();
            }

            return result;
        }
    }
}
