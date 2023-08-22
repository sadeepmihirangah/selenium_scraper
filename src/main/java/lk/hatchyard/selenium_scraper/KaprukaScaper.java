package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KaprukaScaper {

    public static void main(String[] args) {

        final String url = "https://www.kapruka.com/srilanka_online_shopping.jsp?d=sunlight%20soap%20-%20110g";

        Document document = request(url);

        //System.out.println(document);

        Elements elements = document.select("a.catalogueV2Repeater ");

        int count = 0;

        for(Element divElement : elements) {
            count++;

            if(count == 1)
                continue;

            //System.out.println(divElement);

            String productName = divElement.select("div.catalogueV2heading").text();
            System.out.println("Product name : " + productName);

            Elements link = divElement.select("a[href]");
            final String productLink =  link.attr("href");
            System.out.println("Product link : " + productLink);

            String productPrice = divElement.select("span.catalogueV2Local").text();
            System.out.println("Product price : " + productPrice);

            String stockAvailability = divElement.select("div.catalogueV2Button").text();
            System.out.println("Stock Availability : " + stockAvailability);

            Document productDocument = request(productLink);

            //System.out.println(productDocument);

            //System.out.println(productDocument.select("div.product-information.paddingReducerMobile1"));

            Elements productDetails = productDocument.select("div.product-information.paddingReducerMobile1"); // Replace with your div's class name or other identifying attribute
            System.out.println(productDetails.select("h1").first().text());

            System.out.println(productDetails.select("div.price").text());

            //productDetails.select("div.greentxt").text().split("  ");

            Arrays.stream(productDetails.select("div.greentxt").text().split("(?=\\p{Lu})")).forEach(System.out::println);

            System.out.println(productDetails.select("div.info-wrap").select("p").text());

            Elements brandElement = productDetails.select("div.info-wrap").select("a[href]");
            System.out.println("https://www.kapruka.com/" + brandElement.attr("href"));

            System.out.print(productDetails.select("div.info-wrap").select("b").text());
            System.out.println(brandElement.text());

            //System.out.print(productDocument.select("div.view-product"));

            Elements heroImageElement = productDocument.select("div.view-product").select("img[src]");
            System.out.println("https://www.kapruka.com" + heroImageElement.attr("src"));

            Elements imageElements = productDocument.select("div.owl-item");
            Set<String> uniqeImages = new HashSet<>();

            for(Element imageElement : imageElements) {
                Elements images = imageElement.select("div.item").select("img[src]");
                //System.out.println("https://www.kapruka.com" + images.attr("src"));
                uniqeImages.add("https://www.kapruka.com" + images.attr("src"));
            }

            uniqeImages.forEach(System.out::println);

            System.out.println(productDocument.selectXpath("/html/body/div[2]/div/div[1]/div/div[2]/p").text());

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
