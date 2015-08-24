package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ben on 8/9/2015.
 */
public class MeetingControllerPageObject {
    private WebDriver driver;
    private ReadPropertyFile readProps = null;

    public MeetingControllerPageObject(WebDriver newDriver) {

        driver = newDriver;

        try {
            readProps = new ReadPropertyFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void endMeeting() {
        Actions action = new Actions(driver);
        action.moveToElement(meetingControlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
        action.release().perform();
    }

    @FindBy(how= How.XPATH, using="/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")
    private WebElement meetingControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen38']/a")
    private WebElement audioControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen39']/a")
    private WebElement videoControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen40']/a")
    private WebElement presentationControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen41']/a")
    private WebElement desktopShareControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen42']/a")
    private WebElement whiteboardControlsMenu;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen43']/a")
    private WebElement webControlsMenu;

    /* Meeting Controls Menu ************************************************************/
    // Send Participant Invite -----------------------------------------------------------
    @FindBy(how= How.XPATH, using="/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li/div/div/ul/li")
    private WebElement sendParticipantInviteLink;
    @FindBy(how= How.CSS, using="input[id='new_invite_part_name']")
    private WebElement inviteParticipantName;
    @FindBy(how= How.CSS, using="input[id='new_invite_part_email']")
    private WebElement inviteParticipantEmail;
    @FindBy(how= How.CSS, using="button[id='addinvitepart-button']")
    private WebElement addParticipantButton;
    @FindBy(how= How.CSS, using="button[id='sendinvites-button']")
    private WebElement sendInvitesButton;

    // Polls -----------------------------------------------------------------------------
    @FindBy(how= How.XPATH, using="/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li/div/div/ul/li/li")
    private WebElement pollsLink;
    @FindBy(how= How.XPATH, using="/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li/div/div/ul/li/li/div/div/ul/li")
    private WebElement startVotingLink;
    @FindBy(how= How.XPATH, using="/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li/div/div/ul/li/li/div/div/ul/li/li")
    private WebElement stopVotingLink;
    @FindBy(how= How.XPATH, using="/html/body/div[5]/div/div")
    private WebElement chooseAPollDialog;
    @FindBy(how= How.CSS, using="#pollType")
    private WebElement selectPollType;
    @FindBy(how= How.CSS, using="#simpleerrordialogok-button")
    private WebElement cancelPollButton;

    // Capture Meeting Snapshot ----------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen109']/a")
    private WebElement captureMeetingSnapshotLink;
    @FindBy(how= How.XPATH, using=".//*[@id='8709219_emailinput']")
    private WebElement meetingSnapshotEmailInput;
    @FindBy(how= How.CSS, using="#genericdualoptdialogopt1-button")
    private WebElement meetingSnapshotSendButton;
    @FindBy(how= How.CSS, using="#genericdualoptdialogopt2-button")
    private WebElement meetingSnapshotCancelButton;

    // Add New Document ------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen110']/a")
    private WebElement addNewDocumentLink;
    @FindBy(how= How.XPATH, using=".//*[@id='savedocbutton_1440394003629-button']")
    private WebElement saveDocumentButton;

    // End Meeting -----------------------------------------------------------------------
    @FindBy(how= How.CSS, using="button[id='genericdualoptdialogopt1-button']")
    private WebElement endMeetingButton;
    @FindBy(how= How.CSS, using="#endsession")
    private WebElement endSessionRadioButton;
    @FindBy(how= How.CSS, using="#endall")
    private WebElement endAllRadioButton;
    @FindBy(how= How.CSS, using="#genericdualoptdialogopt2-button")
    private WebElement cancelEndMeetingButton;


    /*** Audio Controls Menu ************************************************************/
    //



    public void inviteParticipant() {
        WebElement mtgCtrlsMenu = driver.findElement(By.xpath("/html/body/div[8]/div/div[2]/div[2]/div/div/div/ul/li")); // .//*[@id='yui-gen37']/a
        Actions action = new Actions(driver);
        action.moveToElement(mtgCtrlsMenu).click().moveByOffset(0, 32).clickAndHold().perform();
        action.release().perform();

        WebElement invitePartName = driver.findElement(By.cssSelector("input[id='new_invite_part_name'"));
        invitePartName.sendKeys(new String("AutoTest-Gmail"));
        WebElement invitePartEmail = driver.findElement(By.cssSelector("input[id='new_invite_part_email'"));
        invitePartEmail.sendKeys(new String(readProps.getParticipantEmail()));
        WebElement addPartButton = driver.findElement(By.cssSelector("button[id='addinvitepart-button']"));
        addPartButton.click();

        WebElement sendInvitesButton = driver.findElement(By.cssSelector("button[id='sendinvites-button']"));
        sendInvitesButton.click();
    }

}
