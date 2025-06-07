package page;

public class TermFrequencyPageProcessor extends PageProcessor {

    public TermFrequencyPageProcessor(double relevanceScore) {
        super(relevanceScore);
    }

    @Override
    public boolean process(String page) {
        return false;
    }
}
