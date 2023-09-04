package lk.hatchyard.selenium_scraper;

import okhttp3.*;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.Scanner;

public class UberScraper {

    public static void main(String[] args) throws InterruptedException {
        callAPI ();
        /*System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        options.addArguments("start-maximized"); // Maximize the browser window
        //options.addArguments("disable-infobars"); // Disable browser info bars
        //options.addArguments("--disable-extensions"); // Disable browser extensions
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)\"\n" +
                "+\"AppleWebKit/537.36 (KHTML, like Gecko)\"\n" +
                "+\"Chrome/87.0.4280.141 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);

        // Navigate to the website
        driver.get("https://www.ubereats.com/lk/store/celeste-daily-colombo-05/TxX4NHBQStyKXNuu-OHwAA?diningMode=DELIVERY&entryPoint=shoppingList&ps=1&sc=SEARCH_SUGGESTION&storeSearchQuery=sunlight");
//        driver.get("https://www.ubereats.com/lk/store/celeste-daily-colombo-05/TxX4NHBQStyKXNuu-OHwAA/e830c59d-559e-4155-a189-f89af6274acd/bb7fd913-bf75-47c2-8acd-c7d1ec42c765/6aa4b71f-a795-4fbe-b55c-9ab53082595b");

        String secondPageUrl = "";

        try {

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBox = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location-typeahead-location-manager-input")));
            searchBox.sendKeys("Borella");
            Thread.sleep(1000);
            searchBox.sendKeys(Keys.RETURN);
            //searchBox.sendKeys(Keys.RETURN);

            System.out.println(driver.getCurrentUrl());

//            System.out.println(driver.getPageSource());



        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Don't forget to close the WebDriver after you're done
            //driver.quit();
        }*/
    }

    private static Document request(String url) {

        try {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

            ChromeOptions options=new ChromeOptions();
            //options.addArguments("headless");

            WebDriver webDriver = new ChromeDriver(options);
            webDriver.get(url);

            String content = webDriver.getPageSource();

            //content = content.substring(content.indexOf("<body style"), content.indexOf("<script type=\"text/javascript\">"));

            //System.out.println(content);

            Document doc = Jsoup.parse(content);

            //webDriver.quit();

            return doc;

        } catch (Exception e) {
            return null;
        }
    }

    private static void  callAPI (){
        OkHttpClient client = new OkHttpClient();

        System.out.println("Celeste Daily    1");
        System.out.println("Dropoff          2");
        Scanner input = new Scanner(System.in);
        System.out.print("Select Shop you need :");
        int num = input.nextInt();
        ClassPathResource classPathResource;
        if (num == 1)
            classPathResource = new ClassPathResource("celestedata.json");
        else
            classPathResource = new ClassPathResource("dropoffdata.json");
        RequestBody requestBody;

        try (InputStream inputStream = classPathResource.getInputStream()) {

            // Read the JSON content from the input stream
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            requestBody = RequestBody.create(jsonContent, JSON);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        StringBuilder url = new StringBuilder("https://www.ubereats.com/_p/api/getInStoreSearchV1?localeCode=lk");

        Request request = new Request.Builder()
                .url(String.valueOf(url))
                .post(requestBody)
                .header("X-Csrf-Token","x")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {

                    String jsonStr = responseBody.string();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    System.out.println(jsonObject.getString("status"));

                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject catalogSectionsMap = data.getJSONObject("catalogSectionsMap");
                    Iterator keys = catalogSectionsMap.keys();
                    JSONArray catalogs = null;
                    JSONArray catalogItems = null;

                    while (keys.hasNext()){
                        String key = (String) keys.next();

                        catalogs = catalogSectionsMap.getJSONArray(key);
                    }

                    for (int i = 0; i < catalogs.length(); i++){
                        JSONObject items = catalogs.getJSONObject(i);
                        catalogItems = items.getJSONObject("payload").getJSONObject("standardItemsPayload").getJSONArray("catalogItems");
                    }

                    for (int i = 0; i < catalogItems.length(); i++){
                        JSONObject catalogItem = catalogItems.getJSONObject(i);

                        String title = catalogItem.getString("title");
                        System.out.println("Title :" + title);

                        String itemDescription = catalogItem.getString("itemDescription");
                        System.out.println("Description :" + itemDescription);

                        String price = catalogItem.getJSONObject("priceTagline").getString("accessibilityText");
                        System.out.println("Price :" + price);

                        String imageUrl = catalogItem.getString("imageUrl");
                        System.out.println(imageUrl);

                        System.out.println();
                    }
                    
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
