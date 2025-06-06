import crawler.WebCrawler;
import crawler.BreathFirstSearchWebCrawler;
import page.JavaJobsPageProcessor;

import java.util.Set;
import java.util.logging.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        WebCrawler c = new BreathFirstSearchWebCrawler(new JavaJobsPageProcessor());
        c.crawl(Set.of("https://dev.bg/"));

        while (c.getActiveCrawlTasksCount() != 0) {
            Thread.sleep(5000);
            logger.info("Active crawl tasks %s".formatted(c.getActiveCrawlTasksCount()));
        }
    }
}
