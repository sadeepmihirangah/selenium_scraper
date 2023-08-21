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

public class CargillsScraper {

    public static void main(String[] args) {

        final String url = "https://cargillsonline.com/Web/ProductDetails?ID=Ou0loNrHy9qfJ688FcSU3Q==";

        Document document = request(url);

        //System.out.println(document.select("div.product-detail.pl-5.pr-5.pt-3.pb-4"));

        Elements productDetail = document.select("div.product-detail-right");

        System.out.println(productDetail.select("div.product-detail-right"));
        System.out.println(productDetail.select("div.veg1").first().select("p.ng-binding").text());
    }

    private static Document request(String url) {

        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("start-maximized"); // Maximize the browser window
            //options.addArguments("disable-infobars"); // Disable browser info bars
            //options.addArguments("--disable-extensions"); // Disable browser extensions
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)\"\n" +
                    "+\"AppleWebKit/537.36 (KHTML, like Gecko)\"\n" +
                    "+\"Chrome/87.0.4280.141 Safari/537.36");


            WebDriver webDriver = new ChromeDriver(options);
            webDriver.get(url);

            //WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(4));
            //wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete';"));

            /*String elementId = "root";
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));*/

            /*WebElement myDynamicElement =
                    new WebDriverWait(webDriver, Duration.ofSeconds(4)).until(
                            ExpectedConditions.presenceOfElementLocated(By.id("root")));
            System.out.println(myDynamicElement.isDisplayed());

            System.out.println(myDynamicElement.getText());*/

            String content = webDriver.getPageSource();

            //System.out.println(content);

            //content = content.substring(content.indexOf("<body"), content.indexOf("</body>"));
            content = content.substring(content.indexOf("<body"), content.indexOf("<div class=\"footer2"));

            //System.out.println(content);

            Document doc = Jsoup.parse(content);

            webDriver.quit();

            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
