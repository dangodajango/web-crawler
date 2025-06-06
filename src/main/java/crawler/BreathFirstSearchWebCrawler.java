package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import page.PageProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Collections.newSetFromMap;

public class BreathFirstSearchWebCrawler implements WebCrawler {

    private final PageProcessor pageProcessor;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final AtomicInteger activeCrawlTasks = new AtomicInteger(0);

    private final Set<String> crawledUrls = newSetFromMap(new ConcurrentHashMap<>());

    public BreathFirstSearchWebCrawler(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    @Override
    public int getActiveCrawlTasksCount() {
        return activeCrawlTasks.get();
    }

    @Override
    public void crawl(Set<String> uris) {
        uris.parallelStream()
                .forEach(this::crawl);
    }

    private void crawl(String uri) {
        try {
            HttpRequest webPageUri = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .build();
            crawledUrls.add(normalizeUri(uri));
            activeCrawlTasks.incrementAndGet();
            httpClient.sendAsync(webPageUri, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> processPageContent(Jsoup.parse(response.body()), uri))
                    .whenComplete((test, test2) -> activeCrawlTasks.decrementAndGet());
        } catch (URISyntaxException uriSyntaxException) {
            logger.log(Level.WARNING, "Invalid URI %s".formatted(uri), uriSyntaxException);
            crawledUrls.remove(uri);
            activeCrawlTasks.decrementAndGet();
        }
    }

    private void processPageContent(Document parsedPageContent, String uri) {
        Elements anchors = parsedPageContent.body().getElementsByTag("a");
        String pageContent = parsedPageContent.body().text();
        var points = pageProcessor.process(pageContent);
        if (points == 0.0) {
            return;
        }
        logger.info(uri + " Contains java");
        var urisExtractedFromCurrentPage = anchors.stream()
                .map(anchor -> anchor.attr("href"))
                .filter(this::canUriBeCrawled)
                .collect(Collectors.toSet());
        crawl(urisExtractedFromCurrentPage);
    }

    private boolean canUriBeCrawled(String uri) {
        return !(uri.isBlank()
                 || uri.equals("#")
                 || crawledUrls.contains(normalizeUri(uri)));
    }

    private String normalizeUri(String uri) {
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        }
        return uri;
    }
}