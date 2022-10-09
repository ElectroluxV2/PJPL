package pl.edu.pjwstk.pjpl.scrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.Duration;

public class Utils {
    public static String storageRoot = System.getenv("PJPL.storage");
    public static String humanReadableFormat(final long milliseconds) {
        return Duration
                .ofMillis(milliseconds)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static String removeLastChars(final String str, final int chars) {
        return str.substring(0, str.length() - chars);
    }

    private static String sanitizePathString(final String path) {
        return Normalizer.normalize(path, Normalizer.Form.NFD)
                .replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("[^a-zA-Z0-9-_.]", "_");
    }

    public static File getStorage(final String semester, final String study, final String group) throws IOException {
        final var storage =  Path.of(
                storageRoot,
                sanitizePathString(semester),
                sanitizePathString(study),
                sanitizePathString(group) + ".json"
        ).toFile();

        storage.getParentFile().mkdirs();
        storage.createNewFile();

        return storage;
    }
}
