package crawler;

import page.PageProcessor;

import java.util.Set;

public class DepthFirstSearchWebCrawler extends WebCrawler {

    public DepthFirstSearchWebCrawler(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    @Override
    public void crawl(Set<String> uris) {

    }
}
