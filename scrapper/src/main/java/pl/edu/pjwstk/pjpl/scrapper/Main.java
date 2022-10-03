package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main {
    public static final WebDriver driver = new ChromeDriver();
    public static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(6000));
    public static void main(final String[] args) {
        System.out.println("Welcome to PJPL Scrapper!");

        SemesterParser.parse();

        driver.manage().timeouts().implicitlyWait(Duration.ofNanos(2000));
        driver.quit();
    }
}
