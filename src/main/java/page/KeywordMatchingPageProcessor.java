package page;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeywordMatchingPageProcessor extends PageProcessor {

    private final Map<String, Double> relevanceScoreTable;

    public KeywordMatchingPageProcessor(Map<String, Double> relevanceScoreTable, double minimumRelevanceScore) {
        super(minimumRelevanceScore);
        this.relevanceScoreTable = relevanceScoreTable;
    }

    @Override
    public boolean process(String page) {
        Set<String> encounteredKeywords = new HashSet<>();
        double relevanceScore = Arrays.stream(page.split(" "))
                .map(String::toLowerCase)
                .filter(word -> relevanceScoreTable.containsKey(word) && encounteredKeywords.add(word))
                .map(relevanceScoreTable::get)
                .reduce(Double::sum)
                .orElse(0.0);
        return relevanceScore >= minimumRelevanceScore;
    }
}
