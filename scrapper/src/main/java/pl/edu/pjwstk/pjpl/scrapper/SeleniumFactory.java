package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.logging.Level;

public class SeleniumFactory {
    public static WebDriver makeDriver() {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.WARNING);

        DriverService.Builder<ChromeDriverService, ChromeDriverService.Builder> serviceBuilder = new ChromeDriverService.Builder().withSilent(true);
        ChromeDriverService chromeDriverService = serviceBuilder.build();
        try {
            chromeDriverService.sendOutputTo(new FileOutputStream("webdriver.logs"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        final var options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--log-level=3");

        return new ChromeDriver(chromeDriverService, options);
    }

    public static WebDriverWait makeWait(final WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(2000));
    }
}
