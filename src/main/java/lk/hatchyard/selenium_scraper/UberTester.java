package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UberTester {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        options.addArguments("start-maximized"); // Maximize the browser window
        //options.addArguments("disable-infobars"); // Disable browser info bars
        //options.addArguments("--disable-extensions"); // Disable browser extensions
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)\"\n" +
                "+\"AppleWebKit/537.36 (KHTML, like Gecko)\"\n" +
                "+\"Chrome/87.0.4280.141 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);

        // Navigate to the website
        driver.get("https://www.ubereats.com/lk/feed?diningMode=DELIVERY&pl=JTdCJTIyYWRkcmVzcyUyMiUzQSUyMlBldHRhaCUyMiUyQyUyMnJlZmVyZW5jZSUyMiUzQSUyMkNoSUp0MXBvOXg5WjRqb1I1UFJIbDBrMkFQRSUyMiUyQyUyMnJlZmVyZW5jZVR5cGUlMjIlM0ElMjJnb29nbGVfcGxhY2VzJTIyJTJDJTIybGF0aXR1ZGUlMjIlM0E2LjkzNjcwMDIlMkMlMjJsb25naXR1ZGUlMjIlM0E3OS44NTM0ODIxJTdE");

        String secondPageUrl = "";

        try {

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBox = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-suggestions-typeahead-input")));
            searchBox.sendKeys("dove");
            searchBox.sendKeys(Keys.RETURN);

            //System.out.println(driver.getCurrentUrl());

            //WebElement stores = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/main/div/div/div[2]/div/div[2]")));
            //WebElement stores = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main-content\"]/div/div/div[2]/div/div[2]")));

            //WebElement mainContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-content")));
            //List<WebElement> mainContents = driverWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.id("main-content"), By.cssSelector("he o1 hg hh hi hj hk")));


            /*String elementSource = mainContent.getAttribute("outerHTML");
            System.out.println(elementSource);*/

            //System.out.println(mainContent.getAttribute("innerHTML"));

            secondPageUrl = driver.getCurrentUrl();

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Don't forget to close the WebDriver after you're done
            driver.quit();
        }

        driver = new ChromeDriver(options);
        driver.get(secondPageUrl);

        //WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //WebElement mainContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main-content")));
        //WebElement mainContent = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ch gn cj cx b1")));

        WebElement mainContent = driver.findElement(By.cssSelector("ch gn cj cx b1"));

        while (!isDisplayed(mainContent))
        {
            Thread.sleep(3000);
            System.out.println("Element is not visible yet");
        }

        Document productDocument = request(secondPageUrl);

        System.out.println(productDocument);
    }

    public static boolean isDisplayed(WebElement element) {
        try {
            if(element.isDisplayed())
                return element.isDisplayed();
        }catch (NoSuchElementException ex) {
            return false;
        }
        return false;
    }

    private static Document request(String url) {

        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

            ChromeOptions options=new ChromeOptions();
            options.addArguments("headless");

            WebDriver webDriver = new ChromeDriver(options);
            webDriver.get(url);

            String content = webDriver.getPageSource();

            //content = content.substring(content.indexOf("<body style"), content.indexOf("<script type=\"text/javascript\">"));

            //System.out.println(content);

            Document doc = Jsoup.parse(content);

            webDriver.quit();

            return doc;

        } catch (Exception e) {
            return null;
        }
    }
}
