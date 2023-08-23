package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class UstoreScrapper {

    public static void main(String[] args) {

        final String url = "https://www.ustore.lk/search?type=product&q=Sunlight*";

        Document document = request(url);

        //System.out.println(document);

        Elements searchResultDev = document.select("ul.productgrid--items");

        Elements searchCards = searchResultDev.select("div.productitem");

        for(Element divElement : searchCards) {

            //System.out.println(divElement);

            Elements link = divElement.select("a[href]");
            final String productLink =  "https://www.ustore.lk/" + link.attr("href");
            System.out.println("Product link : " + productLink);

            System.out.println("\nImages,");

            Elements images = divElement.select("img[src]");

            for (Element image : images) {
                System.out.println(image.attr("src").replace("//", ""));
            }

            System.out.println("");

            /*Elements image2 = divElement.select("img[srcset]");
            System.out.println(image2.attr("srcset").replace("//", ""));*/

            Elements productCardInfo = divElement.select("div.productitem--info");

            System.out.println(productCardInfo.select("div.price__compare-at").text());
            System.out.println(productCardInfo.select("div.price__current").text());

            System.out.println(productCardInfo.select("h2.productitem--title ").text());

            System.out.println("");

            Document productDocument = request(productLink);

            Element allProductInfo = productDocument.select("article.product--outer.product_page").first();

            Elements productImages = allProductInfo.select("div.product-gallery--image-background");

            for (Element image : productImages) {
                Elements productImage = image.select("img[src]");
                System.out.println(productImage.attr("src").replace("//", ""));

                Elements productImageLarge = image.select("img[srcset]");
                System.out.println(productImageLarge.attr("srcset").replace("//", ""));
            }

            System.out.println("");

            Elements mainProductInfo = allProductInfo.select("div.product-main");

            System.out.println(mainProductInfo.select("h1.product-title").text());

            System.out.println(mainProductInfo.select("span.product__badge").text());

            System.out.println(mainProductInfo.select("div.price__compare-at").text());
            System.out.println(mainProductInfo.select("div.price__current").text());

            System.out.println(mainProductInfo.select("div.ecom.product-description").text());


            Element video = productDocument.select("div.product-video-row").first();
            System.out.println(video.select("source[src]").attr("src"));

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
