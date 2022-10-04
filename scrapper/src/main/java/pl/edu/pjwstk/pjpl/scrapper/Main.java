package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main {
    public static final WebDriver driver = new ChromeDriver();
    public static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
    public static void main(final String[] args) throws InterruptedException {
        System.out.println("Welcome to PJPL Scrapper!");

        final var groupSchedulePage = GroupSchedulePage
                .open();

        final var semesterSelector = groupSchedulePage
                .openSemesterSelector();

        final var availableSemesters = semesterSelector
                .listAvailableSemesters();

        semesterSelector
                .chooseSemester(availableSemesters.get(0));

        final var studiesSelector = groupSchedulePage
                .openStudySelector();

        final var availableStudies = studiesSelector
                .listAvailableStudies();

        studiesSelector
                .chooseStudy(availableStudies
                        .stream()
                        .filter(study -> study.contains("Informatyka niestacjonarne Gdańsk"))
                        .findFirst()
                        .orElseThrow()
                );

        final var availableGroups = groupSchedulePage
                .getGroupSelector()
                .listAvailableGroups();

        groupSchedulePage
                .getGroupSelector()
                .chooseGroup(availableGroups
                        .stream()
                        .filter(group -> group.contains("54c"))
                        .findFirst()
                        .orElseThrow()
                );

        groupSchedulePage
            .openDatePicker()
            .openMonthView()
            .selectMonth(10)
            .selectYear(2022)
            .apply()
            .chooseFirstAvailableDay()
            .printCurrentDate();

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        Thread.sleep(Duration.ofMinutes(2));

        driver.quit();
    }
}
