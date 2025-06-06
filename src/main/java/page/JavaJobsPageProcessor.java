package page;

import java.util.Arrays;

public class JavaJobsPageProcessor implements PageProcessor {

    private static final String[] KEYWORDS = {"java"};

    @Override
    public double process(String page) {
        if (Arrays.stream(KEYWORDS)
                .anyMatch(c -> page.toLowerCase().contains(c))) {
            return 1;
        }
        return 0.0;
    }
}
