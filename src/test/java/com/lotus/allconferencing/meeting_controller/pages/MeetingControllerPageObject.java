package com.lotus.allconferencing.meeting_controller.pages;

import com.lotus.allconferencing.ReadPropertyFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

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

    public enum ControlsMenu {
        MEETINGCONTROLS(0), AUDIOCONTROLS(1), VIDEOCONTROLS(2), PRESENTATION(3),
        DESKTOPSHARE(4), WHITEBOARD(5), WEB(6);

        private int menuIndex;

        ControlsMenu(int value) { this.menuIndex = value; }

        public int value() { return menuIndex; }
    }

    private WebElement selectControlsMenu(ControlsMenu controlsMenu) {
        WebElement webElement = null;

        switch (controlsMenu) {
            case MEETINGCONTROLS:
                webElement = meetingControlsMenu;
                break;
            case AUDIOCONTROLS:
                webElement = audioControlsMenu;
                break;
            case VIDEOCONTROLS:
                webElement = videoControlsMenu;
                break;
            case PRESENTATION:
                webElement = presentationControlsMenu;
                break;
            case DESKTOPSHARE:
                webElement = desktopShareControlsMenu;
                break;
            case WHITEBOARD:
                webElement = whiteboardControlsMenu;
                break;
            case WEB:
                webElement = webControlsMenu;
                break;
        }
        return webElement;
    }

    public enum MeetingControlsSubmenu {
        SENDINVITE(0), POLLS(1), CAPTUREMEETINGSNAPSHOT(2), ADDNEWDOCUMENT(3), ENDMEETING(4);

        private int meetingControlsSubmenuIndex;

        MeetingControlsSubmenu(int value) { this.meetingControlsSubmenuIndex = value; }

        public int value() { return meetingControlsSubmenuIndex; }
    }

    private WebElement selectControlsSubmenu(MeetingControlsSubmenu controlsSubmenu) {
        WebElement webElement = null;

        switch (controlsSubmenu) {
            case SENDINVITE:
                webElement = sendParticipantInviteLink;
                break;
            case POLLS:
                webElement = pollsLink;
                break;
            case CAPTUREMEETINGSNAPSHOT:
                webElement = captureMeetingSnapshotLink;
                break;
            case ADDNEWDOCUMENT:
                webElement = addNewDocumentLink;
                break;
            case ENDMEETING:
                webElement = endMeetingButton;
                break;
        }
        return webElement;
    }


    /* Status & Controls Menu Bar *******************************************************
    * These are the Menus and Submenus (and in some cases, associated interfaces) of
    * components accessed via the Status & Controls panel.
     */
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
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen109']/a")
    private WebElement endMeetingSubmenuOption;
    @FindBy(how= How.CSS, using="button[id='genericdualoptdialogopt1-button']")
    private WebElement endMeetingButton;
    @FindBy(how= How.CSS, using="#endsession")
    private WebElement endSessionRadioButton;
    @FindBy(how= How.CSS, using="#endall")
    private WebElement endAllRadioButton;
    @FindBy(how= How.CSS, using="#genericdualoptdialogopt2-button")
    private WebElement cancelEndMeetingButton;



    /*** Audio Controls Menu ************************************************************/
    // Refresh Meeting -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen116']/a")
    private WebElement refreshMeetingMenuOption;

    // Talker Notification -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen117']/a")
    private WebElement talkerNotificationMenuOption;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen135']/a")
    private WebElement startTalkerNotification;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen136']/a")
    private WebElement stopTalkerNotification;

    // Security -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen118']/a")
    private WebElement securityMenuOption;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen137']/a")
    private WebElement lockMeeting;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen138']/a")
    private WebElement unlockMeeting;

    // Mute/Unmute Meeting -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen119']/a")
    private WebElement muteMeeting;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen120']/a")
    private WebElement unmuteMeeting;

    // Meeting Rocording -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen121']/a")
    private WebElement meetingRecordingMenuOption;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen139']/a")
    private WebElement startRecording;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen140']/a")
    private WebElement stopRecording;

    // Close Audio Bridge -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen122']/a")
    private WebElement closeAudioBridge;



    /*** Video Controls Menu ************************************************************/
    // Video Menu Options -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen141']/a")
    private WebElement enableVideoMenuOption;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen143']/a")
    private WebElement selectStandardResolution;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen144']/a")
    private WebElement select720pResolution;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen145']/a")
    private WebElement select1080pResolution;
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen142']/a")
    private WebElement disableVideoMenuOption;

    // Video Panel -----------------------------------------------------------------------
    @FindBy(how= How.CSS, using="#videodialog_h")
    private WebElement videoPanelTitleBar;
    @FindBy(how= How.CSS, using=".container-close")
    private WebElement hideVideoPanel;
    @FindBy(how= How.CSS, using="#btn_hangup")
    private WebElement endVideoButton;
    @FindBy(how= How.CSS, using="#btn_fullscreen")
    private WebElement fullscreenButton;
    @FindBy(how= How.CSS, using="#call-status")
    private WebElement videoConnectionStatus;
    @FindBy(how= How.CSS, using="#participant_focus_menubutton-button")
    private Select selectFocusedParticipantSelect;


    /*** Presentation Controls Menu ************************************************************/
    // Presentation Menu Options -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen146']/a")
    private WebElement addNewPresentationMenuOption;
    @FindBy(how= How.CSS, using="#presentationname")
    private WebElement presentationNameInput;
    @FindBy(how= How.CSS, using="#presentationdesc")
    private WebElement presentationDescriptionInput;
    @FindBy(how= How.CSS, using="#presentation_type_menubutton-button")
    private Select presentationTypeSelect;
    @FindBy(how= How.CSS, using=".//*[@id='yui-gen149']/a")
    private Select presentationPDFTypeSelectOption;
    @FindBy(how= How.CSS, using=".//*[@id='yui-gen150']/a")
    private Select presentationTypeImagesSelectOption;
    @FindBy(how= How.CSS, using="#convertimg")
    private WebElement convertImagesCheckbox;
    @FindBy(how= How.CSS, using="#dialogaddpresentation-button")
    private WebElement addPresentationButton;



    /*** Desktop Share Controls Menu ************************************************************/
    // Desktop Share Menu Options -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen151']/a")
    private WebElement desktopShareMenuOption;


    /*** Whiteboard Controls Menu ************************************************************/
    // Whiteboard Controls Menu Options -----------------------------------------------------------------------
    @FindBy(how= How.XPATH, using=".//*[@id='yui-gen42']/a")
    private WebElement startWhiteboardMenuOption;


    public void inviteParticipant(ControlsMenu menu) {
        Actions action = new Actions(driver);
        action.moveToElement(meetingControlsMenu).click().moveByOffset(0, 32).clickAndHold().perform();
        action.release().perform();
    }

    public void endMeeting() {
        Actions action = new Actions(driver);
        action.moveToElement(meetingControlsMenu).click().moveByOffset(0, 105).clickAndHold().perform();
        action.release().perform();
        endMeetingButton.click();
    }





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
