package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DarazScraper {

    public static void main(String[] args) {

        final String url = "https://www.daraz.lk/catalog/?spm=a2a0e.pdp.search.1.60822097ITqAzn&q=sunlight&_keyori=ss&from=search_history&sugg=sunlight_0_1";

        Document document = request(url);

        //System.out.println(document);

        Elements elements = document.select("div.inner--SODwy ");

        for(Element divElement : elements) {

            String productName = divElement.select("div.title--wFj93").text();
            System.out.println("Product name : " + productName);

            Elements link = divElement.select("a[href]");
            String productLink =  link.attr("href");
            productLink = "https:"+productLink;
            System.out.println("Product link : " + productLink);

            String productPrice = divElement.select("span.currency--GVKjl").text();
            System.out.println("Product price : " + productPrice);

            Document productDocument = request(productLink);

            System.out.println(productDocument.select("span.pdp-mod-product-badge-title").text());

            System.out.println(productDocument.select("div.pdp-product-price").text());

            System.out.println(productDocument.select("div.tag-name").text());

            Elements heroImageElement = productDocument.select("div.gallery-preview-panel__content").select("img[src]");
            System.out.println("https:" + heroImageElement.attr("src"));


            System.out.println(productDocument.select("div.pdp-review-summary").text());

            System.out.println(productDocument.select("div.html-content.detail-content").text());

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
