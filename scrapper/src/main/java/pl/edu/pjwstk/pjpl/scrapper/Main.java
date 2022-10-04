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

        try {
            logic();

            Thread.sleep(Duration.ofSeconds(10));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static void logic() {
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

        final var calendarView = groupSchedulePage
                .openDatePicker()
                .openMonthView()
                .selectMonth(10)
                .selectYear(2022)
                .apply()
                .chooseFirstAvailableDay();

        System.out.printf("Scrapping date: %s.%n", calendarView.getCurrentDate());

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        final var availableSubjects = calendarView
                .getAvailableSubjects();

        final var subjectPopout = calendarView
                .openSubjectPopout(availableSubjects.get(0));

        final var studentsCount = subjectPopout
                .getStudentCount();

        System.out.println(studentsCount.getTotal());
        System.out.println(studentsCount.getItn());
        System.out.println(subjectPopout.getSubjectCode());
        System.out.println(subjectPopout.getSubjectType());
        System.out.println(subjectPopout.getGroups());
        System.out.println(subjectPopout.getLectures());
        System.out.println(subjectPopout.getLocation());
        System.out.println(subjectPopout.getRoom());
        System.out.println(subjectPopout.getFrom());
        System.out.println(subjectPopout.getTo());
        System.out.println(subjectPopout.getDuration());
        System.out.println(subjectPopout.getTeamsCode());
    }
}
