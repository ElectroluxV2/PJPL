package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.edu.pjwstk.pjpl.scrapper.calendarview.CalendarView.subjectPopoutBy;

public class Main {
    public static final WebDriver driver;

     static {
         final var options = new ChromeOptions();
         options.addArguments("--headless");
         options.addArguments("--disable-gpu");
         options.addArguments("--window-size=1920,1080");

         driver = new ChromeDriver(options);
     }

    public static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
    public static void main(final String[] args) throws InterruptedException {
        System.out.println("Welcome to PJPL Scrapper!");

        try {
            logic();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        System.out.println("PJPL Scrapper is done with that.");
    }

    public static long countMaximumWork() {
        AtomicInteger maximumWork = new AtomicInteger();

        final var groupSchedulePage = GroupSchedulePage
                .open();

        final var availableSemesters = groupSchedulePage
                .openSemesterSelector()
                .listAvailableSemesters()
                .stream().filter(s -> s.contains("2023")).toList();

        maximumWork.addAndGet(availableSemesters.size());

        availableSemesters.forEach(semester -> {
            groupSchedulePage
                    .openSemesterSelector()
                    .chooseSemester(semester);

            final var availableStudies = groupSchedulePage
                    .openStudySelector()
                    .listAvailableStudies()
                    .stream()
                    .filter(s -> s.contains("Informatyka niestacjonarne Gdańsk"))
                    .toList();

            maximumWork.addAndGet(availableStudies.size());

            availableStudies.forEach(study -> {
                groupSchedulePage
                        .openStudySelector()
                        .chooseStudy(study);

                final var availableGroups = groupSchedulePage
                        .getGroupSelector()
                        .listAvailableGroups();

                maximumWork.addAndGet(availableGroups.size() * 96); // 96 weeks for each group
                System.out.printf("Counting work to do, counted so far: %d.%n", maximumWork.get());
            });
        });

        return maximumWork.get();
    }

    public static void logic() {
        final var maximumWork = countMaximumWork();
        var doneWork = 0;
        final var startTime = System.currentTimeMillis();

        final var groupSchedulePage = GroupSchedulePage
                .open();

        final var semesterSelector = groupSchedulePage
                .openSemesterSelector();

        final var availableSemesters = semesterSelector
                .listAvailableSemesters()
                .stream().filter(s -> s.contains("2023")).toList();

        for (int semesterIndex = 0; semesterIndex < availableSemesters.size(); semesterIndex++) {
            final var semester = availableSemesters.get(semesterIndex);
            doneWork++;

//            groupSchedulePage
//                    .refresh();

            groupSchedulePage
                    .openSemesterSelector()
                    .chooseSemester(semester);

            final var studiesSelector = groupSchedulePage
                    .openStudySelector();

            final var availableStudies = studiesSelector
                    .listAvailableStudies()
                    .stream()
                    .filter(s -> s.contains("Informatyka niestacjonarne Gdańsk"))
                    .toList();


            //                .stream()
            //                .filter(study -> study.contains("Informatyka niestacjonarne Gdańsk"))
            for (int studyIndex = 0; studyIndex < availableStudies.size(); studyIndex++) {
                final var study = availableStudies.get(studyIndex);
                doneWork++;

                groupSchedulePage
                        .openStudySelector()
                        .chooseStudy(study);

                final var availableGroups = groupSchedulePage
                        .getGroupSelector()
                        .listAvailableGroups();


                //                .stream()
                //                .filter(group -> group.contains("54c"))
                for (int groupIndex = 0; groupIndex < availableGroups.size(); groupIndex++) {
                    doneWork++;
                    final var group = availableGroups.get(groupIndex);
                    groupSchedulePage
                            .getGroupSelector()
                            .chooseGroup(group);

                    final var calendarView = groupSchedulePage
                            .openDatePicker()
                            .openMonthView()
                            .selectMonth(1)
                            .selectYear(Integer.parseInt(semester.split("/")[0]))
                            .apply()
                            .chooseFirstAvailableDay();


                    // Every week for 2 years forward
                    final var totalWeeks = 2 * 12 * 4;
                    for (int weekIndex = 0; weekIndex < totalWeeks; weekIndex++) {
                        doneWork++;
                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

                        final var currentlyProcessed = doneWork + 4;
                        final var percentDoneCurrentGroup = (double) currentlyProcessed / maximumWork * 100;
                        final var timeSpent = System.currentTimeMillis() - startTime;
                        final var estimatedTimeCurrentGroup = (maximumWork - currentlyProcessed) * (timeSpent / currentlyProcessed);

                        System.out.printf("Scrapping, time: %s (%05.2f%%) Est. %s - semester (%d/%d): %s, study (%d/%d): %s, group (%d/%d): %s, week (%d/%d): %s.%n",
                                humanReadableFormat(Duration.ofMillis(timeSpent)),
                                percentDoneCurrentGroup, humanReadableFormat(Duration.ofMillis(estimatedTimeCurrentGroup)),
                                semesterIndex + 1, availableSemesters.size(), semester,
                                studyIndex + 1, availableStudies.size(), study,
                                groupIndex + 1, availableGroups.size(), group,
                                weekIndex + 1, totalWeeks, calendarView.getCurrentDate()
                        );

                        final var availableSubjects = calendarView
                                .getAvailableSubjects();

                        for (final var availableSubject : availableSubjects) {
                            final var subjectPopout = calendarView
                                    .openSubjectPopout(availableSubject);

                            System.out.println("-----------");
                            System.out.println(driver.findElement(subjectPopoutBy).getText());
                            System.out.println("-----------");

//                            final var studentsCount = subjectPopout
//                                    .getStudentCount();
//
//                            System.out.println(studentsCount.getTotal());
//                            System.out.println(studentsCount.getItn());
//                            System.out.println(subjectPopout.getSubjectCode());
//                            System.out.println(subjectPopout.getSubjectType());
//                            System.out.println(subjectPopout.getGroups());
//                            System.out.println(subjectPopout.getLectures());
//                            System.out.println(subjectPopout.getLocation());
//                            System.out.println(subjectPopout.getRoom());
//                            System.out.println(subjectPopout.getFrom());
//                            System.out.println(subjectPopout.getTo());
//                            System.out.println(subjectPopout.getDuration());
//                            System.out.println(subjectPopout.getTeamsCode());

                            subjectPopout.close();
                        }

                        calendarView.goToNextWeek();
                    }
                }
            }
        }
    }

    public static String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
