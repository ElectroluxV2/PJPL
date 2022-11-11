package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SeleniumFactory {
    private static final String runTemplate = "docker run --rm -d --name chromium_%d -p 127.0.0.1:%d:4444 -e SE_SESSION_REQUEST_TIMEOUT=36000 -e SE_NODE_SESSION_TIMEOUT=36000 -e SE_NODE_MAX_SESSIONS=8 -e SE_START_XVFB=false -m 2G --shm-size 2g docker.io/seleniarm/standalone-chromium:latest";
    private static final String stopTemplate = "docker stop chromium_%d";
    private static final ConcurrentHashMap<Integer, Boolean> ports = new ConcurrentHashMap<>();

    static {
        for (int i = 3000; i <= 3100; i++) {
            ports.put(i, false);
        }
    }

    private static Integer getFreePort() {
        final var port = ports
                .entrySet()
                .stream()
                .filter(e -> !e.getValue())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find free port for new webdriver container in range 3000-3100"))
                .getKey();
        ports.put(port, true);
        return port;
    }

    public static WebDriver makeDriver() throws MalformedURLException {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.WARNING);

        final var options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--log-level=3");

        final var freePort = SeleniumFactory.getFreePort();
        final var url = new URL("http://localhost:%d".formatted(freePort));
        runContainer(freePort);

        return new ContainerizedWebDriver(url, options, freePort);
    }

    private static void runContainer(int port) {
        Scrapper.LOGGER.info("Running new webdriver container on port: %d.".formatted(port));
        ports.put(port, true);

        try {
            final var proc = Runtime.getRuntime().exec(runTemplate.formatted(port, port));
            proc.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            Scrapper.LOGGER.fine("StdOut (%d): %s.".formatted(port, stdInput.lines().collect(Collectors.joining())));
            Scrapper.LOGGER.fine("StdError (%d): %s.".formatted(port, stdError.lines().collect(Collectors.joining())));

            // Wait for start
            final var check = new URI("http://localhost:%d/wd/hub/status".formatted(port));
            final var client = HttpClient.newHttpClient();
            final var request = HttpRequest.newBuilder().uri(check).build();

            var response = "";
            while (!response.contains("true")) {
                try {
                    System.out.println(request.uri());

                    response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                } catch (Exception ignore) { }
            }

            Scrapper.LOGGER.info("Started new webdriver container on port: %d.".formatted(port));
        } catch (IOException | URISyntaxException | InterruptedException e) {
            Scrapper.LOGGER.severe("Can't start webdriver container on port: %d.".formatted(port));
            throw new RuntimeException(e);
        }
    }

    private static void stopContainer(int port) {
        try {
            final var proc = Runtime.getRuntime().exec(stopTemplate.formatted(port));
            proc.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            Scrapper.LOGGER.fine("StdOut (%d): %s.".formatted(port, stdInput.lines().collect(Collectors.joining())));
            Scrapper.LOGGER.fine("StdError (%d): %s.".formatted(port, stdError.lines().collect(Collectors.joining())));

            Scrapper.LOGGER.info("Stopped webdriver container on port: %d.".formatted(port));
        } catch (IOException | InterruptedException e) {
            Scrapper.LOGGER.severe("Can't stop webdriver container on port: %d.".formatted(port));
            throw new RuntimeException(e);
        }

        ports.put(port, false);
    }

    public static WebDriverWait makeWait(final WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(2000));
    }

    public static class ContainerizedWebDriver extends RemoteWebDriver {
        private final int port;

        public ContainerizedWebDriver(URL remoteAddress, Capabilities capabilities, int port) {
            super(remoteAddress, capabilities);
            this.port = port;
        }

        @Override
        public void close() {
            super.close();
            SeleniumFactory.stopContainer(port);
        }
    }
}
