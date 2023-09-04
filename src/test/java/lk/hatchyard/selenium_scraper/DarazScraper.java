package lk.hatchyard.selenium_scraper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
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

            if(count == 2)
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
            String productId = "";
            for(Element element : productDetails){
                String SKU = element.select("div.html-content.key-value").text();
                String key = element.select("span.key-title").text();
                if (key.equals("SKU")) {
                    productId = SKU.split("_")[0];
                }
                System.out.println("SKU :" + SKU);
            }


            Elements heroImageElement = productDocument.select("div.next-slick-slide.next-slick-active.item-gallery__thumbnail").select("img[src]");
            Set<String> uniqeImages = new HashSet<>();
            for(Element imageElement : heroImageElement) {
                uniqeImages.add("https:" + imageElement.attr("src"));
            }
            uniqeImages.forEach(System.out::println);


            System.out.println(productDocument.select("div.pdp-review-summary").text());

            System.out.println(productDocument.select("div.html-content.detail-content").text());



            callAPI(productId);

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

    private static void  callAPI ( String productID){
        OkHttpClient client = new OkHttpClient();

//        String url = "https://my.daraz.lk/pdp/review/getReviewList?itemId=102444728&pageSize=5&filter=0&sort=0";
        String pageSize = "pageSize=5";
        String itemId = "itemId="+productID;
        String filter = "filter=0&sort=0";
        StringBuilder url = new StringBuilder("https://my.daraz.lk/pdp/review/getReviewList?");
        url.append(itemId);
        url.append("&"+pageSize);
        url.append("&"+filter);
//        System.out.println(url);

        Request request = new Request.Builder()
                .url(String.valueOf(url))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String jsonStr = responseBody.string();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    // Now you can work with the jsonObject as needed
                    JSONObject model = jsonObject.getJSONObject("model");
                    JSONArray jsonArray = model.getJSONArray("items");
//                    JSONArray jsonArray = new JSONArray(items);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject rate = jsonArray.getJSONObject(i);
                        String reviewContent = rate.getString("reviewContent");
                        String rating = rate.getString("rating");

                        System.out.println("Review Content: " + reviewContent + "  Rating: " + rating);
                    }

//                    System.out.println(jsonObject.toString(4)); // Print with indentation
                } else {
                    System.out.println("Empty response body.");
                }
            } else {
                System.out.println("API request failed with response code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
