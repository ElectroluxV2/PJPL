package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.edu.pjwstk.pjpl.scrapper.components.GroupSchedulePage;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(final String[] args) {
        System.out.println("Welcome to PJPL Scrapper!");

        final var driver = SeleniumFactory.makeDriver();
        final var wait = SeleniumFactory.makeWait(driver);

        try {
            logic(driver, wait);
        } catch (final Exception exception) {
            exception.printStackTrace(System.err);
        } finally {
            driver.quit();
        }

        System.out.println("PJPL Scrapper is done with that.");
    }


    public static void logic(final WebDriver driver, final WebDriverWait wait) {
        final var schedulePage = GroupSchedulePage.open(driver, wait);
        final var semester = "2022/2023 zimowy";
        final var study = "Informatyka niestacjonarne Gdańsk";

        schedulePage
                .openSemesterSelector()
                .chooseSemester(semester);

        schedulePage
                .openStudySelector()
                .chooseStudy(study);

        final var availableGroups = schedulePage
                .getGroupSelector()
                .listAvailableGroups()
                .stream()
//                .limit(2)
            ;
        AtomicInteger id = new AtomicInteger();
        final var scrappers = availableGroups
                .map(group -> new GroupScrapper(semester, study, group, id.incrementAndGet()));

        try (final var service = Executors.newCachedThreadPool()) {
            scrappers.forEach(service::submit);
        }
    }
}
