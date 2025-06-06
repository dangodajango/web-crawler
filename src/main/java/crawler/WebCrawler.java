package crawler;

import page.PageProcessor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.newSetFromMap;

public abstract class WebCrawler {

    protected final PageProcessor pageProcessor;

    protected final double minimumRelevanceScore;

    protected final Set<String> crawledUrls = newSetFromMap(new ConcurrentHashMap<>());

    public abstract void crawl(Set<String> uris);

    public abstract int getActiveCrawlTasksCount();

    protected WebCrawler(PageProcessor pageProcessor, double minimumRelevanceScore) {
        this.pageProcessor = pageProcessor;
        this.minimumRelevanceScore = minimumRelevanceScore;
    }

    protected boolean canUriBeCrawled(String uri) {
        return !(uri.isBlank()
                 || uri.equals("#")
                 || crawledUrls.contains(normalizeUri(uri)));
    }

    protected String normalizeUri(String uri) {
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        }
        return uri;
    }
}
