package com.lotus.allconferencing;

import net.lingala.zip4j.exception.ZipException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 9/3/2015.
 */
public abstract class BaseSeleniumTest {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;

    private static ChromeDriverFactory chromeDriverFactory = new ChromeDriverFactory();

    public enum BrowserName {FIREFOX, CHROME, IE, OPERA, SAFARI, SAUCELABS, HTMLUNIT}

    public static WebDriver setDriver(BrowserName browser) {

        switch (browser) {
            case FIREFOX:
                driver = new FirefoxDriver();
                break;

            case CHROME:
                try {
                    driver = chromeDriverFactory.getChromeDriver();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ZipException e) {
                    e.printStackTrace();
                }
                break;

            case IE:
                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ieCapabilities.setCapability("requireWindowFocus", true);
                ieCapabilities.setCapability("ie.ensureCleanSession", true);

                driver = new InternetExplorerDriver(ieCapabilities);
                break;

            case OPERA:
                driver = new OperaDriver();
                break;

            case SAFARI:
                driver = new SafariDriver();
                break;

            case SAUCELABS:
                DesiredCapabilities sauceCapabilities = DesiredCapabilities.firefox();
                sauceCapabilities.setCapability("version", "5");
                sauceCapabilities.setCapability("platform", Platform.XP);
                try {
                    // add url to environment variables to avoid releasing with source
                    String sauceURL = System.getenv("SAUCELABS_URL");
                    driver = new RemoteWebDriver(
                            new URL(sauceURL),
                            sauceCapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case HTMLUNIT:
                driver = new HtmlUnitDriver();
                break;
        }
        return driver;
    }

    public static WebDriver getDriver() {

        if(driver != null) {
            return driver;
        }

        return new FirefoxDriver();
    }

    public static ReadPropertyFile getSettings() {
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProps;
    }

    @Parameterized.Parameters
    public static Collection dataFromExcel() throws IOException {
        InputStream spreadsheet = new FileInputStream("src/test/resources/allconftest_data.xlsx");
        return new SpreadsheetData(spreadsheet).getData();
    }

    public static class SpreadsheetData {

        private transient Collection<Object[]> data = null;

        public SpreadsheetData(final InputStream excelInputStream) throws IOException {
            this.data = loadFromSpreadsheet(excelInputStream);
        }

        public Collection<Object[]> getData() {
            System.out.println("Value of 'data' in getData(): " + data);
            return data;
        }

        private Collection<Object[]> loadFromSpreadsheet(final InputStream excelFile) throws IOException {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

            data = new ArrayList<Object[]>();
            XSSFSheet sheet = workbook.getSheetAt(2);

            int numberOfColumns = countNonEmptyColumns(sheet);
            List<Object[]> rows = new ArrayList<>();
            List<Object> rowData = new ArrayList<>();

            // Setup new for loop based on int, then assign sheet.getRow(int) to row (see Bas' example - On Test Automation)
            for(int i = 0; i <= sheet.getLastRowNum(); i++) {
                i++;
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
