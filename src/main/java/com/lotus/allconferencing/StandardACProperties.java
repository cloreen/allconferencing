package com.lotus.allconferencing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Ben on 8/5/2015.
 */
public class StandardACProperties {

    public static void main(String args[]) {

        Properties properties = new Properties();
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream("C:/IntelliJ_Projects/allconferencing/src/main/resources/allconf_standard.properties");

            // Values for Moderator
            properties.setProperty("client_id1", "25784");
            properties.setProperty("acpassword1", "lotus456");


            // Values for Participant
            properties.setProperty("participant_testemail1", "bgactest01@gmail.com");
            properties.setProperty("participant_emailpwd1", "lotus12345");


            // Values for Second Participant
            properties.setProperty("participant_testemail2", "bgactest03@gmail.com");
            properties.setProperty("participant_emailpwd2", "lotus12345");


            // Values for Second Moderator Account
            properties.setProperty("client_id2", "25792");
            properties.setProperty("acpassword2", "lotus123");



            // save properties file
            properties.store(outputStream, null);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }
}
