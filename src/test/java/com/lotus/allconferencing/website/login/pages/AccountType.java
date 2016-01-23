package com.lotus.allconferencing.website.login.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.WebDriver;

/**
 * Created by Ben on 1/18/2016.
 */
public class AccountType {
    private static WebDriver driver;
    private static ReadPropertyFile readProps = null;
    private static AcctType acctType;

    public enum LoginType {
        STANDARD(0), CORPORATE(1), PARTICIPANT(2);

        private int loginIndex;

        LoginType(int value) {
            this.loginIndex = value;
        }

        public int value() {
            return loginIndex;
        }
    }

    public enum AcctType {
        STANDARD_OLD(0), STANDARD_SIMPLE(1), STANDARD_NEW(2), CORPORATE(3), PARTICIPANT(4);

        private int acctTypeIndex;

        AcctType(int value) {
            this.acctTypeIndex = value;
        }

        public int value() {
            return acctTypeIndex;
        }
    }

    public AccountType(WebDriver newDriver, AcctType acctType) {
        driver = newDriver;
        acctType = acctType;
        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
