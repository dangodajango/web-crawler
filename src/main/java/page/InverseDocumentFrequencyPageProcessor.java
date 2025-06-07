package page;

public class InverseDocumentFrequencyPageProcessor extends PageProcessor {

    public InverseDocumentFrequencyPageProcessor(double relevanceScore) {
        super(relevanceScore);
    }

    @Override
    public boolean process(String page) {
        return false;
    }
}