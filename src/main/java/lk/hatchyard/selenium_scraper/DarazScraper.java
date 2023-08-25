package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashSet;
import java.util.Set;

public class DarazScraper {

    public static void main(String[] args) {

        final String url = "https://www.daraz.lk/catalog/?spm=a2a0e.pdp.search.1.60822097ITqAzn&q=sunlight&_keyori=ss&from=search_history&sugg=sunlight_0_1";

        Document document = request(url);

        //System.out.println(document);

        Elements elements = document.select("div.inner--SODwy ");

        int count = 0;

        for(Element divElement : elements) {
            count++;

            if(count == 1)
                continue;

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

            Elements productDetails = productDocument.select("li.key-li");
            for(Element element : productDetails){
                System.out.println(element.select("div.html-content.key-value").text());
            }


            Elements heroImageElement = productDocument.select("div.next-slick-slide.next-slick-active.item-gallery__thumbnail").select("img[src]");
            Set<String> uniqeImages = new HashSet<>();
            for(Element imageElement : heroImageElement) {
                uniqeImages.add("https:" + imageElement.attr("src"));
            }
            uniqeImages.forEach(System.out::println);


            System.out.println(productDocument.select("div.pdp-review-summary").text());

            System.out.println(productDocument.select("div.html-content.detail-content").text());

            Elements reviews = productDocument.select("div#module_product_review.pdp-block.module").select("div.lazyload-wrapper ");
            Set<String> comments = new HashSet<>();
            for(Element commentElement : reviews) {
                comments.add("comment :" + commentElement.select("div.content").text());
            }
            comments.forEach(System.out::println);

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
