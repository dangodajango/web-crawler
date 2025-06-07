package page;

public class TermFrequencyInverseDocumentFrequencyPageProcessor extends PageProcessor {

    public TermFrequencyInverseDocumentFrequencyPageProcessor(double relevanceScore) {
        super(relevanceScore);
    }

    @Override
    public boolean process(String page) {
        return false;
    }
}