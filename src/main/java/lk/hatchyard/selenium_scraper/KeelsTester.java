package lk.hatchyard.selenium_scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class KeelsTester {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Navigate to the website
        driver.get("https://www.keellssuper.com/home");

        try {
            // Locate the search input field and enter a search query
            WebElement searchBox = driver.findElement(By.className("auto-complete-text")); // Change the locator accordingly
            searchBox.sendKeys("Sunlight");

            // Locate the search button and trigger the click event
            WebElement searchButton = driver.findElement(By.className("product-search-button")); // Change the locator accordingly
            searchButton.click();

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
