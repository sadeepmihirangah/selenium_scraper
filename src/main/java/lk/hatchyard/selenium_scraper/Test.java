package lk.hatchyard.selenium_scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {

    public static void main(String[] args) {

        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";

        String html2 = "<body><p>Parsed HTML into a doc.</p></body>";

        Document doc = Jsoup.parse(html2);

        System.out.println(doc.text());
    }
}
