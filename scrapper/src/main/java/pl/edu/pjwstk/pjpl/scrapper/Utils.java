package pl.edu.pjwstk.pjpl.scrapper;

import java.time.Duration;

public class Utils {
    public static String humanReadableFormat(final long milliseconds) {
        return Duration
                .ofMillis(milliseconds)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
