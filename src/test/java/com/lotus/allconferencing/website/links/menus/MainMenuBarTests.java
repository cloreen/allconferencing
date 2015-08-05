package com.lotus.allconferencing.website.links.menus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Ben on 5/2/2015.
 */
public class MainMenuBarTests {

    private static WebDriver driver;
    private static String currentPage, homePageTitle, newPageTitle, newPageContent = "";
    private static String homeURL = "http://www.allconferencing.com";
    private static List<WebElement> webElements;
    private static int webElementsSize;
    private static By locator;

    public WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public String checkNewPageContent(WebElement element, int pos, int subpos) {

        String base = driver.getWindowHandle();

        System.out.println("Base window handle is: " + base);

        Actions actions = new Actions(driver);
        actions.contextClick(element).perform();
        actions.sendKeys(new String("w")).perform();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        Set<String> set = driver.getWindowHandles();
        String myHandle = "";
        String otherHandle = "";
        for (String item : set) {
            i++;
            System.out.println("Handle of Window " + i + " is: " + item);
            if (i == 1) {
                myHandle = item.toString();
            } else {
                otherHandle = item.toString();
            }
        }

        set.remove(base);
        assert (set.size() == 1);

        driver.switchTo().window(otherHandle);

        if (pos == 1) {
            WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>td[class='faq_page_H1']"));
            newPageContent = contentElement.getText();
            System.out.println(newPageContent);
        } else if (pos == 2) {
            WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>td[class='advanced_page_H1']"));
            newPageContent = contentElement.getText();
            System.out.println(newPageContent);
        } else if (pos == 3) {
            WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>td[id='horizontalLine']"));
            newPageContent = contentElement.getText();
            System.out.println(newPageContent);
        } else if (pos == 4) {
            WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>th[class='coach_indexpage_h1']"));
            newPageContent = contentElement.getText();
            System.out.println(newPageContent);
        } else if (pos == 5) {
            if (subpos == 0) {
                WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>td[class='faq_page_H1']"));
                newPageContent = contentElement.getText();
                System.out.println(newPageContent);
            } else if (subpos != 4) {
                WebElement contentElement = driver.findElement(By.cssSelector("tbody>tr>td>h1[class='faq_page_H2']"));
                newPageContent = contentElement.getText();
                System.out.println(newPageContent);
            } else if (subpos == 4) {
                // do nothing
            }

            //WebElement contentElement = driver.findElement(By.cssSelector()
            //       } else {
            //           checkNewPageTitle(by, pos);
        }




        /*

        driver.switchTo().window(String.valueOf(set.toArray(new String[0])));
*/
        driver.close();
        driver.switchTo().window(base);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Actions actions1 = new Actions(driver);
        if (pos < 4) {
            actions1.moveByOffset(0, -30);
        } else {
            actions1.moveByOffset(-40, 0);
        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("newPageContent value is: " + newPageContent);
        return newPageContent;
    }


    public String getMainMenuBarElementPosition(List<WebElement> webElementsList, int webElementsIndex, int subMenuElementsIndex) {
        int elementPosition = 0;
        By menuBarlocator = By.cssSelector("ul[id='MenuBar1']>li>a");
        By submenuInvisibleLocator = By.cssSelector("ul[id='MenuBar1']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();
        String newPageContent = "";

        if (webElementsIndex == 0) {
            // do nothing
        } else if (webElementsIndex <= 3) {
            newPageContent = checkNewPageContent(element, webElementsIndex, 0);
        } else if (webElementsIndex == 4) {
            newPageContent = subMenuHandler(element, elementText, submenuInvisibleLocator, webElementsIndex, subMenuElementsIndex);
        } else if (webElementsIndex == 5) {
            newPageContent = subMenuHandler(element, elementText, submenuInvisibleLocator, webElementsIndex, subMenuElementsIndex);
        } else {
            newPageContent = "Sorry, that page was not found.";
        }
        return newPageContent;
    }

    public String subMenuHandler(WebElement mainMenuElement, String elementText, By subMenuLocator, int webElementsIndex, int subMenuElementIndex) {
        WebElement expertHostedEvents = driver.findElement(By.cssSelector("ul>li>a[class='MenuBarItemSubmenu']"));
        By by = By.cssSelector("ul>li>a[class='MenuBarItemSubmenu']");
        String menuOptionText, newPageTitle, expertHostedEventsText = "";
        Actions actions = new Actions(driver);
        actions.click(mainMenuElement);

        if (webElementsIndex == 4) {
            By aboutExpertHostedBy = By.cssSelector("ul>li>ul[class='MenuBarSubmenuVisible']>li>a");
            List<WebElement> subMenuList = driver.findElements(by);
            System.out.println(subMenuList.size());
            subMenuList.get(0).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<WebElement> expertHostedList = driver.findElements(aboutExpertHostedBy);
            System.out.println("Size of ExpertHostedList: " + expertHostedList.size());

            WebElement aboutExpertElement = driver.findElement(aboutExpertHostedBy);
            return checkNewPageContent(aboutExpertElement, 4, 0);


/*
            Actions expertActions = new Actions(driver);
            expertActions.contextClick(aboutExpertElement).perform();
            expertActions.sendKeys(new String("w")).perform();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //aboutExpertElement.click();
            */
        }

        if (webElementsIndex == 5) {
            By faqByFAQ = By.cssSelector("ul>li>ul[class='MenuBarSubmenuVisible']>li>a");
            List<WebElement> subMenuList = driver.findElements(by);
            if (subMenuElementIndex == 0) {
                System.out.println(subMenuList.size());
                subMenuList.get(1).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WebElement> faqList = driver.findElements(faqByFAQ);
                System.out.println("Size of FAQList: " + faqList.size());

                WebElement faqFAQ = faqList.get(0);
                //System.out.println("newPageContent is: " + checkNewPageContent(faqFAQ, 5, 0));
                return checkNewPageContent(faqFAQ, 5, 0);

            } else if (subMenuElementIndex == 1) {
                subMenuList.get(1).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WebElement> faqList = driver.findElements(faqByFAQ);
                System.out.println("Size of ExpertHostedList: " + faqList.size());

                WebElement faqGlossary = faqList.get(1);
                //System.out.println("newPageContent is: " + checkNewPageContent(faqGlossary, 5, 1));
                return checkNewPageContent(faqGlossary, 5, 1);

            } else if (subMenuElementIndex == 2) {
                subMenuList.get(1).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WebElement> faqList = driver.findElements(faqByFAQ);
                System.out.println("Size of ExpertHostedList: " + faqList.size());

                WebElement faqAboutConf = faqList.get(2);
                //System.out.println("newPageContent is: " + checkNewPageContent(faqAboutConf, 5, 2));
                return checkNewPageContent(faqAboutConf, 5, 2);

            } else if (subMenuElementIndex == 3) {
                subMenuList.get(1).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WebElement> faqList = driver.findElements(faqByFAQ);
                System.out.println("Size of ExpertHostedList: " + faqList.size());

                WebElement faqAboutInvoice = faqList.get(3);
                //System.out.println("newPageContent is: " + checkNewPageContent(faqAboutInvoice, 5, 3));
                return checkNewPageContent(faqAboutInvoice, 5, 3);

            }






/*
            Actions expertActions = new Actions(driver);
            expertActions.contextClick(aboutExpertElement).perform();
            expertActions.sendKeys(new String("w")).perform();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //aboutExpertElement.click();
            */
        }
/*
            WebElement expertHostedElement = subMenuList.get(0);
            newPageContent = checkNewPageContent(aboutExpertHostedBy, 0);
            assertEquals("About Expert Hosed Events page should display",
                    "About Expert Hosted Events (coming soon)", newPageContent);
        } else {
            newPageTitle = checkNewPageTitle(subMenuLocator, 0);
        }
        /*
        for (int i = 0; i < subMenuList.size(); i++) {
            WebElement subMenuElement = subMenuList.get(i);
            menuOptionText = subMenuElement.getText();
            if (webElementsIndex == 4) {
                newPageContent = checkNewPageContent(expertHostedBy, i);
                assertEquals("About Expert Hosted Events page should display",
                        "About Expert Hosted Events (coming soon)", newPageContent);
            } */
        //           }
        ;
        //           expertHostedEventsText = expertHostedEvents.getText();
        //       }
        return newPageContent;
    }


    @BeforeClass
    public static void setup() {
        driver = new FirefoxDriver();
        currentPage = driver.getCurrentUrl().toString();
        if (currentPage != homeURL) {
            driver.navigate().to(homeURL);
        } else {
            // do nothing
        }

        homePageTitle = driver.getTitle();
        locator = By.cssSelector("ul[id='MenuBar1']>li>a");

        webElements = driver.findElements(locator);

        webElementsSize = webElements.size();
    }

    @Test
    public void easeOfUseLink() {
        int webElementsIndex = 1;
        int subMenuElementsIndex = 0;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("The Ease of Use page should display",
                "Easily Meet and Collaborate",
                newPageContent);
    }

    @Test
    public void advancedLink() {
        int webElementsIndex = 2;
        int subMenuElementsIndex = 0;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("The Advanced page should display",
                "Collaborate with Anyone, Anytime, Anywhere, Instantly",
                newPageContent);
    }

    @Test
    public void compareUsLink() {
        int webElementsIndex = 3;
        int subMenuElementsIndex = 0;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("The Compare Us page should display",
                "The Only Enterprise Class Conferencing & " +
                        "Cloud Sharing Service with No Monthly Fees or Minimums",
                newPageContent);
    }

    @Test
    public void aboutExpertHostedEventsLink() {
        int webElementsIndex = 4;
        int subMenuElementsIndex = 0;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("The Compare Us page should display",
                "About Expert Hosted Events (coming soon)",
                newPageContent);
    }

    @Test
    public void frequentlyAskedQuestionsLink() {
        int webElementsIndex = 5;
        int subMenuElementsIndex = 0;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("FAQ text is found", "Browse FAQs", newPageContent);
    }

    @Test
    public void glossaryLink() {
        int webElementsIndex = 5;
        int subMenuElementsIndex = 1;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("Glossary text is found", "Glossary", newPageContent);
    }

    @Test
    public void aboutYourConferenceLink() {
        int webElementsIndex = 5;
        int subMenuElementsIndex = 2;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("About Your Conference text is found", "About Your Conference", newPageContent);
    }

    @Test
    public void aboutYourInvoiceLink() {
        int webElementsIndex = 5;
        int subMenuElementsIndex = 3;
        String newPageContent = "";

        newPageContent = getMainMenuBarElementPosition(webElements, webElementsIndex, subMenuElementsIndex);

        assertEquals("About Your Invoice text is found", "About Your Invoice", newPageContent);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}