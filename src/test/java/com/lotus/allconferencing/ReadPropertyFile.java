package com.lotus.allconferencing;

import org.openqa.selenium.By;

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
    public String getBrowser() { return props.getProperty("browser"); }

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

    // Very old test account--------------------------------------------------------
    public String getOlderAcctClientID() {
        return props.getProperty("oldacct1_clientid");
    }
    public String getOlderAcctPassword() {
        return props.getProperty("oldacct1_password");
    } //----------------------------------------------------------------------------

    // Old test account-------------------------------------------------------------
    public String getOldAcctClientID() {
        return props.getProperty("oldacct2_clientid");
    }
    public String getOldAcctPassword() {
        return props.getProperty("oldacct2_password");
    } //----------------------------------------------------------------------------

    // v2 Scheduled Meeting Test Data-----------------------------------------------
    public String getv2ScheduledConfName() {
        return props.getProperty("v2_conf_name");
    }
    public String getModeratorName() {
        return props.getProperty("mod_name");
    }
    public String getInviteEmailSubject() {
        return props.getProperty("meeting_invite_subject");
    }

}
