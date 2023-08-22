package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LassanaScraper {

    public static void main(String[] args) {

        final String url = "https://lassana.com/search/-1?search-text=sunlight%20yellow";

        Document document = request(url);

        //System.out.println(document);

        Element searchResultDev = document.getElementById("search_result_dev");

        Elements elements = searchResultDev.select("div.list2_item");

        for(Element divElement : elements) {

            Elements searchedProduct = divElement.select("div.product.list_item_card");

            //System.out.println(searchedProduct);

            String productName = divElement.select("p").first().text();
            System.out.println("Product name : " + productName);

            String price = divElement.select("b").text();
            System.out.println("Price : " + price);

            Elements link = searchedProduct.select("a[href]");
            final String productLink =  link.attr("href");
            System.out.println("Product link : https://lassana.com" + productLink);


            Document productDocument = request(productLink);

            break;
        }

        //System.out.println(searchResultDev);
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
