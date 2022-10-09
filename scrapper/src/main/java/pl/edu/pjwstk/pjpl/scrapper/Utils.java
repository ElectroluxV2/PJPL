package pl.edu.pjwstk.pjpl.scrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class Utils {
    public static final JsonMapper mapper;
    static  {
        mapper = JsonMapper
                .builder()
                .findAndAddModules()
                .build();
    }
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

    public static File getStorage(final String... subPaths) throws IOException {
        final File storage = switch (subPaths.length) {
            case 0 -> throw new IllegalStateException("Unexpected value: " + subPaths.length);
            case 1 -> Path.of(
                     storageRoot,
                 sanitizePathString(subPaths[0]) + ".json"
                ).toFile();
            default -> Path.of(
                    storageRoot,
                     Arrays.stream(Arrays.copyOfRange(subPaths, 0, subPaths.length))
                            .map(Utils::sanitizePathString)
                            .toArray(String[]::new)
            ).resolveSibling("%s.json".formatted(sanitizePathString(subPaths[subPaths.length - 1]))).toFile();
        };

        storage.getParentFile().mkdirs();
        storage.createNewFile();

        return storage;
    }

    public static String makeMultiLineList(final List<String> list) {
        final var sj = new StringJoiner("\n");
        list.forEach(s -> sj.add("- `%s`;".formatted(s)));
        return sj.toString();
    }

    public static void updateIndex(final List<String> values, final File file) throws IOException {
        System.out.printf("Updating index file: `%s`.%n", file);

        HashMap<String, String> index;

        try {
            index = mapper.readValue(file, new TypeReference<HashMap<String, String>>() {});
        } catch (final Exception ignore) {
            index = new HashMap<>();
        }


        for (final var value : values) {
            index.put(value, sanitizePathString(value));
        }

        mapper.writeValue(file, index);
    }
}
