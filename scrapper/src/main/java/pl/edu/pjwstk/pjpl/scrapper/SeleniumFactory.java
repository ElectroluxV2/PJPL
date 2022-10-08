package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumFactory {
    public static WebDriver makeDriver() {
        final var options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }

    public static WebDriverWait makeWait(final WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(2000));
    }
}
