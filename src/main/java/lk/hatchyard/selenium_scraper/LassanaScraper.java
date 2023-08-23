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
            final String productLink =  "https://lassana.com" + link.attr("href");
            System.out.println("Product link : " + productLink);


            Document productDocument = request(productLink);

            System.out.println(productDocument.selectXpath("/html/body/div[4]/div[1]/div[1]/div/div[2]/div/div[3]/div[1]/span/p[2]/a"));
            System.out.println(productDocument.selectXpath("/html/body/div[4]/div[1]/div[1]/div/div[2]/div/div[3]/div[1]/span/p[3]/a"));

            System.out.println(productDocument.select("div.product_name").select("h1.p_dark").text());
            System.out.println(productDocument.select("div.product_name").select("p").text());

            //System.out.println(productDocument.select("div.ratings"));
            System.out.println(productDocument.select("div.reviews").select("span").select("p").first().text());

            System.out.println(productDocument.select("p.orig_price").text());

            System.out.println(productDocument.select("div.description").first().text());

            Elements reviews = productDocument.select("div.review_details_block");

            System.out.println("");

            for(Element review : reviews) {
                //System.out.println(review.text());
                System.out.println(review.select("p.user_name_block").text());
                System.out.println(review.select("p.rating_date").text());
                System.out.println(review.select("div.rating_stars").text());
                System.out.println(review.select("p.comment_block").text());

                System.out.println("");
            }

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
