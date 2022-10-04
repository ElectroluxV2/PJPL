package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static pl.edu.pjwstk.pjpl.scrapper.calendarview.CalendarView.subjectPopoutBy;

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

        System.out.println("PJPL Scrapper is done with that.");
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

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        for (int i = 0; i < 20; i++) {
            System.out.printf("Scrapping date: %s.%n", calendarView.getCurrentDate());

            final var availableSubjects = calendarView
                    .getAvailableSubjects();

            for (final var availableSubject : availableSubjects) {
                final var subjectPopout = calendarView
                        .openSubjectPopout(availableSubject);

                System.out.println("-----------");
                System.out.println(driver.findElement(subjectPopoutBy).getText());
                System.out.println("-----------");

//                final var studentsCount = subjectPopout
//                        .getStudentCount();

//                System.out.println(studentsCount.getTotal());
//                System.out.println(studentsCount.getItn());
//                System.out.println(subjectPopout.getSubjectCode());
//                System.out.println(subjectPopout.getSubjectType());
//                System.out.println(subjectPopout.getGroups());
//                System.out.println(subjectPopout.getLectures());
//                System.out.println(subjectPopout.getLocation());
//                System.out.println(subjectPopout.getRoom());
//                System.out.println(subjectPopout.getFrom());
//                System.out.println(subjectPopout.getTo());
//                System.out.println(subjectPopout.getDuration());
//                System.out.println(subjectPopout.getTeamsCode());

                subjectPopout.close();
            }

            calendarView.goToNextWeek();
        }
    }
}
