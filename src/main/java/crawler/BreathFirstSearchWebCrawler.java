package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import page.PageProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BreathFirstSearchWebCrawler extends WebCrawler {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public BreathFirstSearchWebCrawler(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    @Override
    public void crawl(Set<String> uris) {
        uris.parallelStream()
                .forEach(this::crawl);
    }

    private void crawl(String uri) {
        try {
            HttpRequest webPageUri = HttpRequest.newBuilder().uri(new URI(uri)).build();
            crawledUrls.add(normalizeUri(uri));
            activeCrawlTasks.incrementAndGet();
            httpClient.sendAsync(webPageUri, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> processPageContent(Jsoup.parse(response.body())))
                    .whenComplete((result, error) -> activeCrawlTasks.decrementAndGet());
        } catch (URISyntaxException uriSyntaxException) {
            logger.log(Level.WARNING, "Invalid URI %s".formatted(uri), uriSyntaxException);
            crawledUrls.remove(uri);
            activeCrawlTasks.decrementAndGet();
        }
    }

    private void processPageContent(Document parsedPageContent) {
        Elements anchors = parsedPageContent.body().getElementsByTag("a");
        String pageContent = parsedPageContent.body().text();
        if (pageProcessor.process(pageContent)) {
            return;
        }
        Set<String> urisExtractedFromCurrentPage = anchors.stream()
                .map(anchor -> anchor.attr("href"))
                .filter(this::canUriBeCrawled)
                .collect(Collectors.toSet());
        crawl(urisExtractedFromCurrentPage);
    }
}