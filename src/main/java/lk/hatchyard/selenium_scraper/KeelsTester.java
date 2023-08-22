package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KeelsTester {

    public static void main(String[] args) {

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
        driver.get("https://www.keellssuper.com/home");

        try {
            // Locate the search input field and enter a search query
           /* WebElement searchBox = driver.findElement(By.className("auto-complete-text")); // Change the locator accordingly
            searchBox.sendKeys("Sunlight");*/

            //WebElement parentDiv = driver.findElement(By.className("product-search")); // Change the locator accordingly


            // Locate the input (text box) element within the parent div
            //WebElement searchBox = parentDiv.findElement(By.tagName("input"));
            /*WebElement searchBox = driver.findElement(By.xpath("//div[@class='auto-complete-text']//input"));
            searchBox.sendKeys("Sunlight");*/

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='auto-complete-text']//input")));
            searchBox.sendKeys("Sunlight Care Liquid 1 Liter");

            // Locate the search button and trigger the click event
            WebElement searchButton = driver.findElement(By.className("product-search-button")); // Change the locator accordingly
            searchButton.click();

            WebElement productBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='product-card-image-container btn col-md-12']")));
            productBox.click();

            WebElement finalPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='product-card-final-price']")));
            System.out.println(finalPrice.getAttribute("innerHTML"));

            WebElement productPageDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='productName col']")));
            System.out.println(productPageDetails.getAttribute("innerHTML"));

            WebElement orgPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='product-card-original-price']")));
            System.out.println(orgPrice.getAttribute("innerHTML"));

            WebElement heroImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='product-card-container limitMainTile col']")));
            //System.out.println(heroImage.getAttribute("innerHTML"));

            Document heroImageDoc = Jsoup.parse(heroImage.getAttribute("innerHTML"));

            Elements heroImageElement = heroImageDoc.select("img[src]");
            System.out.println(heroImageElement.attr("src"));

            // You can add additional steps to handle the search results or other actions here
            // ...

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Don't forget to close the WebDriver after you're done
            driver.quit();
        }
    }
}
