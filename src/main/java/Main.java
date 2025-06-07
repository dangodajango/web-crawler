import crawler.BreathFirstSearchWebCrawler;
import crawler.WebCrawler;
import page.KeywordMatchingPageProcessor;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Map.entry;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        WebCrawler c = new BreathFirstSearchWebCrawler(new KeywordMatchingPageProcessor(relevanceScoreTable, 8));
        c.crawl(Set.of("https://dev.bg/company/jobs/java/"));

        while (c.getActiveCrawlTasksCount() > 0) {
            Thread.sleep(5000);
            logger.log(Level.INFO, "Active crawl tasks %s".formatted(c.getActiveCrawlTasksCount()));
        }
    }

    private static final Map<String, Double> relevanceScoreTable = Map.ofEntries(
            entry("java", 6.0),
            entry("programming", 3.0),
            entry("spring", 3.0),
            entry("job", 5.0),
            entry("backend", 2.0),
            entry("mid", 4.0),
            entry("sql", 2.0),
            entry("hibernate", 3.0),
            entry("junit", 2.0),
            entry("обяви", 2.0)
    );
}
