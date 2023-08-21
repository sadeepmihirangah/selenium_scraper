package lk.hatchyard.selenium_scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GlomarkScraper {

    public static void main(String[] args) {

        final String url = "https://glomark.lk/search?search-text=sunlight%20soap";

        Document document = request(url);

        Elements elements = document.select("div.product-box.justify-content-center");

        for(Element divElement : elements) {

            Elements link = divElement.select("a[href]");
            final String productLink =  "https://glomark.lk" + link.attr("href");
            String productName = link.text();
            System.out.println("Product name : " + productName);
            System.out.println("Product link : " + productLink);

            Document productDocument = request(productLink);

            //System.out.println(document.text());

            Elements productDetails = productDocument.select("div.details.col-12.col-sm-12.col-md-6.col-lg-5.col-xl-5");

            //System.out.println(divElement);

            System.out.println(productDetails.select("div.product-title").text());

            System.out.println(productDocument.getElementById("product-promotion-price").text());

            System.out.println(productDetails.select("div.product-discount").text());

            System.out.println(productDetails.select("div.discounted-price").text());

            System.out.println(productDetails.select("span.product-availability").text());

            System.out.println(productDetails.select("p.sku").text());

            System.out.println(productDetails.select("p.Category").text());

            System.out.println("\n");
            break;
        }
    }

    private static Document request(String url) {

        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

            ChromeOptions options=new ChromeOptions();
            options.addArguments("headless");

            WebDriver webDriver = new ChromeDriver(options);
            webDriver.get(url);

            String content = webDriver.getPageSource();

            //content = content.substring(content.indexOf("<body"), content.indexOf("</body>"));
            content = content.substring(content.indexOf("<body"), content.indexOf("<script type=\"text/javascript\">"));

            //System.out.println(content);

            Document doc = Jsoup.parse(content);

            webDriver.quit();

            return doc;

        } catch (Exception e) {
            return null;
        }
    }
}
