package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SparScrapper {

    public static void main(String[] args) {
        final String url = "https://www.spar.lk/search?type=product&options%5Bprefix%5D=last&options%5Bunavailable_products%5D=last&q=sunlight";

        Document document = request(url);

        Elements elements = document.select("div.product-item.product-item--vertical");

        int count = 0;

        for(Element divElement : elements) {
            count++;

            if(count == 1)
                continue;

            String productName = divElement.select("a.product-item__title.text--strong.link").text();
            System.out.println("Product name : " + productName);

            Elements link = divElement.select("a[href]");
            String productLink =  link.attr("href");
            productLink = "https://www.spar.lk"+productLink;
            System.out.println("Product link : " + productLink);

            String productPrice = divElement.select("span.price.price--highlight").text();
            System.out.println(productPrice);

            Document productDocument = request(productLink);

            System.out.println("Product name : " + productDocument.
                    select("h1.product-meta__title.heading.h1").text());

            Elements discountLabel = productDocument.select("div.product-meta__label-list");

            System.out.println("Discount : " + discountLabel.
                    select("span.product-label.product-label--on-sale").select("span.money").text());

            Elements priceLabel = productDocument.select("div.card__section").select("div.price-list");

            System.out.println("Sale price: " + priceLabel.select("span.price.price--highlight").select("span.money").text());

            System.out.println("Regular price: " + priceLabel.select("span.price.price--compare").select("span.money").text());

            Elements heroImageElement = productDocument.select("div.product-gallery__carousel.product-gallery__carousel--zoomable").select("img[src]");

            System.out.println("https:" + heroImageElement.attr("src"));


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
