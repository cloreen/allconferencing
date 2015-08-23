package com.lotus.allconferencing;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ben on 8/22/2015.
 */
public class ReadPropertyFile {
    private static String resourceName = "config.properties";
    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static Properties props = new Properties();

    public ReadPropertyFile() throws Exception {
        try (InputStream resourceStream = classLoader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        }
    }

    public String getUrl() {
        return props.getProperty("url");
    }
    // Main test account -------------------------------------------------------
    public String getOwnerClientID() {
        return props.getProperty("owner1_clientid");
    }
    public String getOwnerPassword() {
        return props.getProperty("owner1_password");
    } //-------------------------------------------------------------------------

    // Main participant test account --------------------------------------------
    public String getParticipantEmail() {
        return props.getProperty("part1_email");
    }
    public String getParticipantEmailPwd() {
        return props.getProperty("part1_emailpass");
    }
    public String getParticipantClientID() {
        return props.getProperty("part1_clientid");
    }
    public String getParticipantAcctPwd() {
        return props.getProperty("part1_acctpass");
    } //--------------------------------------------------------------------------

    // Second participant test account--------------------------------------------
    public String getParticipant2Email() {
        return props.getProperty("part2_email");
    }
    public String getParticipant2EmailPwd() {
        return props.getProperty("part2_emailpass");
    }
    public String getParticipant2ClientID() {
        return props.getProperty("part2_clientid");
    }
    public String getParticipant2AcctPwd() {
        return props.getProperty("part2_acctpass");
    } //----------------------------------------------------------------------------

    // Second moderator test account----------------------------------------------------
    public String getMod2Email() {
        return props.getProperty("mod2_email");
    }
    public String getMod2EmailPwd() {
        return props.getProperty("mod2_emailpass");
    } //----------------------------------------------------------------------------

    // Second owner test account----------------------------------------------------
    public String getOwner2ClientID() {
        return props.getProperty("owner2_clientid");
    }
    public String getOwner2Password() {
        return props.getProperty("owner2_password");
    } //----------------------------------------------------------------------------
}
