package com.lotus.allconferencing;

import com.lotus.allconferencing.website.login.pages.AccountType;
import com.lotus.allconferencing.website.login.pages.LoginPageObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ben on 3/18/2016.
 */
public class ExcelData extends BaseSeleniumTest {

    private static Collection<Object[]> excelData;
    private static ArrayList<Object[]> dataArray;
    private static Object[] excelObjectArray;
    private static Object excelObject;
    private static List<Object> dataList;

    public enum Browser {FIREFOX, CHROME, IE, OPERA, SAUCELABS, HTMLUNIT}
    public static AccountType.LoginType loginType;
    public static AccountType.AcctType accountType;
    public static LoginPageObject.AccessType accessType;
    public static Browser browser;

    public ExcelData() {
    }

    public static void getDataFromExcel() {
        try {
            excelData = dataFromExcel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataArray = (ArrayList<Object[]>)excelData;
        for(Object[] objectArray : dataArray) {
            evaluateExcelData(objectArray);
        }
    }

    private static void evaluateExcelData(Object[] inObjArray) {
        int i = 0;
        // Take in objects from object array and get the strings to initialize variables with the proper values
        for(Object object : inObjArray) {
            System.out.println("Object " + object + " in objectArray from Excel sheet is: " + object.toString());
            switch(i) {
                case 0:
                    switch(object.toString()) {
                        case "FIREFOX":
                            browser = Browser.FIREFOX;
                            break;
                        case "CHROME":
                            browser = Browser.CHROME;
                            break;
                        case "IE":
                            browser = Browser.IE;
                            break;
                        case "OPERA":
                            browser = Browser.OPERA;
                            break;
                        case "SAUCELABS":
                            browser = Browser.SAUCELABS;
                            break;
                        case "HTMLUNIT":
                            browser = Browser.HTMLUNIT;
                            break;
                    }
                case 1:
                    switch(object.toString()) {
                        case "STANDARD":
                            loginType = AccountType.LoginType.STANDARD;
                            break;
                        case "CORPORATE":
                            loginType = AccountType.LoginType.CORPORATE;
                            break;
                        case "PARTICIPANT":
                            loginType = AccountType.LoginType.PARTICIPANT;
                            break;
                    }
                    break;
                case 2:
                    switch(object.toString()) {
                        case "STANDARD_OLD":
                            accountType = AccountType.AcctType.STANDARD_OLD;
                            break;
                        case "STANDARD_NEW":
                            accountType = AccountType.AcctType.STANDARD_NEW;
                            break;
                        case "STANDARD_SIMPLE":
                            accountType = AccountType.AcctType.STANDARD_SIMPLE;
                            break;
                    }
                    break;
                case 3:
                    switch (object.toString()) {
                        case "LOGIN":
                            accessType = LoginPageObject.AccessType.LOGIN;
                    }
                    break;
                case 4:
                    LoginPageObject.clientID = object.toString();
                    break;
                case 5:
                    LoginPageObject.clientPass = object.toString();
                    break;
            }
            i++;
        }
    }
}
