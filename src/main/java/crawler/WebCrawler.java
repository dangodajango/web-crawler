package crawler;

import java.util.Set;

public interface WebCrawler {
    void crawl(Set<String> uris);

    int getActiveCrawlTasksCount();
}
