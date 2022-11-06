package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.logging.Level;

public class SeleniumFactory {
    public static WebDriver makeDriver() throws MalformedURLException {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.WARNING);

        final var options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--log-level=3");

        return new RemoteWebDriver(new URL(System.getenv("PJPL_webdriver")), options);
    }

    public static WebDriverWait makeWait(final WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(2000));
    }
}
