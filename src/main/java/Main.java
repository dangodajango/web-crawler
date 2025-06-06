import crawler.BreathFirstSearchWebCrawler;
import crawler.WebCrawler;
import page.JavaJobsPageProcessor;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        WebCrawler c = new BreathFirstSearchWebCrawler(new JavaJobsPageProcessor(), 1);
        c.crawl(Set.of("https://dev.bg/"));

        while (c.getActiveCrawlTasksCount() != 0) {
            Thread.sleep(5000);
            logger.log(Level.INFO, "Active crawl tasks {}", c.getActiveCrawlTasksCount());
        }
    }
}
