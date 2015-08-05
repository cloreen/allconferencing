package com.lotus.allconferencing.website.links;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class HomePageTests {
    private static WebDriver driver;
    private static String currentPage, newPageTitle, newPageContent = "";
    private static String homeURL = "http://www.allconferencing.com";

    public int getNumberOfElementsFound(By by) {
        return driver.findElements(by).size();
    }

    public WebElement getElementWithIndex(By by, int pos) {
        return driver.findElements(by).get(pos);
    }

    public String checkNewPageTitle(By by, int pos) {

        String base = driver.getWindowHandle();

        System.out.println("Base window handle is: " + base);

        Actions actions = new Actions(driver);
        actions.contextClick(getElementWithIndex(by, pos)).perform();
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

        newPageTitle = driver.getTitle();

        System.out.println(newPageTitle);

        /*

        driver.switchTo().window(String.valueOf(set.toArray(new String[0])));
*/
        driver.close();
        driver.switchTo().window(base);
        /*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        return newPageTitle;
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

        if (pos == 4) {
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("newPageContent value is: " + newPageContent);
        return newPageContent;
    }

    public void getLoginMenuElementPosition(List<WebElement> webElementsList, int webElementsIndex) {
        int elementPosition = 0;
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();

        if (webElementsIndex == 0) {
            assertEquals("The login page title matches the index",
                    "Login to All Conferencing - Conference Calls - Reliable Teleconferencing" +
                            " - Share Presentations, Participant Chat", checkNewPageTitle(locator, webElementsIndex));
        } else if (webElementsIndex == 1) {
            assertEquals("The Corp Login page title matches index", "Administrator " +
                    "Login", checkNewPageTitle(locator, webElementsIndex));
        } else if (webElementsIndex == 2) {
            assertEquals("The login page title matches the index",
                    "Login to All Conferencing - Conference Calls - Reliable Teleconferencing" +
                            " - Share Presentations, Participant Chat", checkNewPageTitle(locator, webElementsIndex));
        }
    }

    public void getMainMenuBarElementPosition(List<WebElement> webElementsList, int webElementsIndex) {
        int elementPosition = 0;
        By menuBarlocator = By.cssSelector("ul[id='MenuBar1']>li>a");
        By submenuInvisibleLocator = By.cssSelector("ul[id='MenuBar1']>li>a");
        int acctLoginElementPos = 0;
        int corpLoginElementPos = 0;
        int partLoginElementPos = 0;
        WebElement element = webElementsList.get(webElementsIndex);
        String elementText = element.getText();

        if (webElementsIndex == 0) {
            // do nothing
        } else if (webElementsIndex == 1) {
            assertEquals("The Ease of Use page should display",
                "All Conferencing Services - Audio, Web, Event Conferencing",
                checkNewPageTitle(menuBarlocator, webElementsIndex));
        } else if (webElementsIndex == 2) {
            assertEquals("The Advanced page should display",
                "All Conferencing Account Types - Affordable Conferencing Service",
                checkNewPageTitle(menuBarlocator, webElementsIndex));
        } else if (webElementsIndex == 3) {
            assertEquals("The Compare Us page should display",
                    "All Conferencing - Pricing, Feautres, and Benefits " +
                            "Comparison to AT&T, Verizon, & WebEx.",
                checkNewPageTitle(menuBarlocator, webElementsIndex));
        } else if (webElementsIndex == 4) {
            subMenuHandler(element, elementText, submenuInvisibleLocator, webElementsIndex);
        } else if (webElementsIndex == 5) {
            subMenuHandler(element, elementText, submenuInvisibleLocator, webElementsIndex);
        }
    }

    public void subMenuHandler(WebElement mainMenuElement, String elementText, By subMenuLocator, int webElementsIndex) {
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
            System.out.println("newPageContent is: " + checkNewPageContent(aboutExpertElement, 4, 0));


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
            assertEquals("FAQ text is found", "Browse FAQs", checkNewPageContent(faqFAQ, 5, 0));

            subMenuList.get(1).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Size of ExpertHostedList: " + faqList.size());

            WebElement faqGlossary = faqList.get(1);
            //System.out.println("newPageContent is: " + checkNewPageContent(faqGlossary, 5, 1));
            assertEquals("Glossary text is found", "Glossary", checkNewPageContent(faqGlossary, 5, 1));



            subMenuList.get(1).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Size of ExpertHostedList: " + faqList.size());

            WebElement faqAboutConf = faqList.get(2);
            //System.out.println("newPageContent is: " + checkNewPageContent(faqAboutConf, 5, 2));
            assertEquals("About Your Conference text is found", "About Your Conference", checkNewPageContent(faqAboutConf, 5, 2));


            subMenuList.get(1).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Size of ExpertHostedList: " + faqList.size());

            WebElement faqAboutInvoice = faqList.get(3);
            //System.out.println("newPageContent is: " + checkNewPageContent(faqAboutInvoice, 5, 3));
            assertEquals("About Your Invoice text is found", "About Your Invoice", checkNewPageContent(faqAboutInvoice, 5, 3));
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

    }

    @Before
    public void setup() {
        driver = new FirefoxDriver();
        currentPage = driver.getCurrentUrl().toString();
        if(currentPage != homeURL) {
            driver.navigate().to(homeURL);
        } else {
            // do nothing
        }

    }

    @Test
    public void loginMenuBarLinks() {
        String homePageTitle = driver.getTitle();
        By locator = By.cssSelector("ul[id='MenuBar3']>li>a");

        List<WebElement> webElements;
        webElements = driver.findElements(locator);

        int webElementsSize = webElements.size();

        int webElementsIndex = 0;
        for (webElementsIndex = 0; webElementsIndex < webElementsSize; webElementsIndex++) {
            getLoginMenuElementPosition(webElements, webElementsIndex);
        }
    }


    @Test
    public void menuBarLinks() {
        String homePageTitle = driver.getTitle();
        By menuBarLocator = By.cssSelector("ul[id='MenuBar1']>li>a");

        List<WebElement> webElements;
        webElements = driver.findElements(menuBarLocator);

        int webElementsSize = webElements.size();

        int webElementsIndex = 0;

        for (webElementsIndex = 0; webElementsIndex < webElementsSize; webElementsIndex++) {
            getMainMenuBarElementPosition(webElements, webElementsIndex);
        }







/*
        WebElement menuBar = driver.findElement(By.cssSelector("#MenuBar1"));
//        driver.navigate().to(homeURL);
        WebElement listItem = driver.findElement(By.tagName("li"));
//        System.out.println("The list item found was - " + listItem.getText());
        System.out.println("These are the MenuBar options:");
        List<WebElement> menuBarLinkList = menuBar.findElements(By.xpath("//ul[@id='MenuBar1']/li"));
        for (WebElement element : menuBarLinkList) {
            System.out.println(element.getText());
        }

        System.out.println("");

        System.out.println("These are the submenu options of the MenuBar options:");
//        WebElement nestedLI = expertHostedEvents.findElement(By.xpath("//li[contains(text()='Expert Hosted Events']/ul/li"));
        for (WebElement element : menuBarLinkList) {
            List<WebElement> subMenuList = element.findElements(By.xpath("//ul/li/ul/li"));
            if(subMenuList.size() > 2) {
                element.click();
                for (WebElement subElement : subMenuList) {
                    String elementText = subElement.getText();
                    if (elementText.matches("^[a-zA-z]+(\\Ax0)*[a-zA-Z]+(\\Ax0)*[a-zA-Z]+$")) {
                        System.out.println(elementText);
                    }
                }
            }
        }
*/
/*
        WebElement firstSubmenuOption = driver.findElement(By.xpath("//li/ul/li"));
        System.out.println(firstSubmenuOption.);
/*
        for (WebElement element : subMenuList) {
            System.out.println(element.getText());
        }
*/
//          System.out.println(menuBarLinkList.size());
/*        for(int i = 0; i < menuBarLinkList.size(); i++) {

        }*/
    }


    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
