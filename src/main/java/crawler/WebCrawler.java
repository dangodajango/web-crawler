package crawler;

import page.PageProcessor;

import java.net.http.HttpClient;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.newSetFromMap;

public abstract class WebCrawler {

    protected final HttpClient httpClient = HttpClient.newHttpClient();

    protected final Set<String> crawledUrls = newSetFromMap(new ConcurrentHashMap<>());

    protected final AtomicInteger activeCrawlTasks = new AtomicInteger(0);

    protected final PageProcessor pageProcessor;

    public abstract void crawl(Set<String> uris);

    protected WebCrawler(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public int getActiveCrawlTasksCount() {
        return activeCrawlTasks.get();
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
