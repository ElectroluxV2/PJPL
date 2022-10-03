package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.edu.pjwstk.pjpl.scrapper.datepicker.DatePicker;

import java.time.Duration;

public class Main {
    public static final WebDriver driver = new ChromeDriver();
    public static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
    public static void main(final String[] args) {
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

        DatePicker
            .open()
            .openMonthView()
            .selectMonth(2)
            .selectYear(2020)
            .apply()
            .chooseFirstAvailableDay()
            .printCurrentDate();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
        driver.quit();
    }
}
