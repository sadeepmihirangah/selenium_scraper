package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DarazScrapper {
    public static void main(String[] args) {
        final String url = "https://www.daraz.lk/catalog/?spm=a2a0e.home.search.1.675a4625Gaqknw&q=sunlight&_keyori=ss&from=search_history&sugg=sunlight_0_1";

        Document document = request(url);

        Elements elements = document.select("div.gridItem--Yd0sa");
        int count = 0;

        for(Element divElement : elements) {
            count++;

            if (count == 0)
                continue;

            String productName = divElement.select("div.title-wrapper--IaQ0m").text();
            System.out.println("Product name : " + productName);

            String productRate = divElement.select("span.ratig-num--KNake.rating--pwPrV").text();
            System.out.println("Rate : " + productRate);

            String currentPrice = divElement.select("div.current-price--Jklkc").text();
            System.out.println("Current Price : " + currentPrice);

            String price = divElement.select("div.original-price--lHYOH").text();
            System.out.println("Price : " + price);

            Elements link = divElement.select("a[href]");
            String productLink = link.attr("href");
            productLink = "https:" + productLink;
            System.out.println(productLink);

            Document productDocument = request(productLink);

            String rates = productDocument.select("a.pdp-link.pdp-link_size_s.pdp-link_theme_blue.pdp-review-summary__link").text();
            System.out.println("Rates : " + rates);

            String brand = productDocument.select("a.pdp-link.pdp-link_size_s.pdp-link_theme_blue.pdp-product-brand__brand-link").text();
            System.out.println("Brand : " + brand);

            Elements heroImageElement = productDocument.select("div.gallery-preview-panel__content").select("img[src]");
            System.out.println(heroImageElement.attr("src"));
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
