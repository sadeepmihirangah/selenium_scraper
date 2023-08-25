package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UberScraper {

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
        driver.get("https://www.ubereats.com/lk/store/celeste-daily-colombo-05/TxX4NHBQStyKXNuu-OHwAA/e830c59d-559e-4155-a189-f89af6274acd/bb7fd913-bf75-47c2-8acd-c7d1ec42c765/6aa4b71f-a795-4fbe-b55c-9ab53082595b");

        String secondPageUrl = "";

        try {

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBox = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location-typeahead-location-manager-input")));
            searchBox.sendKeys("Borella");
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.RETURN);
            //searchBox.sendKeys(Keys.RETURN);

            System.out.println(driver.getCurrentUrl());

            System.out.println(driver.getPageSource());

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Don't forget to close the WebDriver after you're done
            //driver.quit();
        }
    }

    private static Document request(String url) {

        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

            ChromeOptions options=new ChromeOptions();
            //options.addArguments("headless");

            WebDriver webDriver = new ChromeDriver(options);
            webDriver.get(url);

            String content = webDriver.getPageSource();

            //content = content.substring(content.indexOf("<body style"), content.indexOf("<script type=\"text/javascript\">"));

            //System.out.println(content);

            Document doc = Jsoup.parse(content);

            //webDriver.quit();

            return doc;

        } catch (Exception e) {
            return null;
        }
    }
}
