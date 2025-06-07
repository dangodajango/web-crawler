package page;

public abstract class PageProcessor {

    protected final double minimumRelevanceScore;

    protected PageProcessor(double relevanceScore) {
        this.minimumRelevanceScore = relevanceScore;
    }

    public abstract boolean process(String page);
}
