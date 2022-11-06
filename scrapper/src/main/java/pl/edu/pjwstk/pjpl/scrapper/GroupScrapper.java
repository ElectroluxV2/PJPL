package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.JavascriptExecutor;
import pl.edu.pjwstk.pjpl.scrapper.components.GroupSchedulePage;
import pl.edu.pjwstk.pjpl.scrapper.components.calendarview.CalendarView;
import pl.edu.pjwstk.pjpl.scrapper.contract.GroupDto;
import pl.edu.pjwstk.pjpl.scrapper.contract.SubjectDto;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static pl.edu.pjwstk.pjpl.scrapper.Utils.*;

public class GroupScrapper implements Runnable {
    private final static int weeksToScrapCount = 4 * 12 * 2; // Every week for 2 years forward
    private final String semester;
    private final String study;
    private final String group;
    private final int id;

    public GroupScrapper(final String semester, final String study, final String group, final int id) {
        this.semester = semester;
        this.study = study;
        this.group = group;
        this.id = id;
    }

    private void log(final String message) {
        System.out.printf("Scrapper [%d] (%s): %s.%n", id, group, message);
    }

    @Override
    public void run() {
        int counter = 0;
        while (counter < 10) {
            try {
                logic();
                break;
            } catch (final IOException exception) {
                log("An error occurred during group `%s` parsing, trying again (%d / 10).".formatted(group, ++counter));
                exception.printStackTrace(System.err);
            }
        }
    }

    private void logic() throws IOException {
        log("Start");

        final File storageFile;
        try {
            storageFile = getStorage(semester, study, group);
        } catch (final IOException exception) {
            log("Failed to open storage");
            exception.printStackTrace(System.err);
            throw new RuntimeException(exception);
        }

        final var driver = SeleniumFactory.makeDriver();
        final var wait = SeleniumFactory.makeWait(driver);
        final var schedulePage = GroupSchedulePage.open(driver, wait);

        schedulePage
                .openSemesterSelector()
                .chooseSemester(semester);

        schedulePage
                .openStudySelector()
                .chooseStudy(study);

        schedulePage
                .getGroupSelector()
                .chooseGroup(group);

        final var calendarView = schedulePage
                .openDatePicker()
                .openMonthView()
                .selectMonth(1)
                .selectYear(Integer.parseInt(semester.split("/")[0]))
                .apply()
                .chooseFirstAvailableDay();

        final var subjects = new ArrayList<SubjectDto>();

        final var timeStart = System.currentTimeMillis();
        for (var weekIndex = 1; weekIndex <= weeksToScrapCount; weekIndex++) {
            final var percentDone = (double) weekIndex / weeksToScrapCount * 100;
            final var timeSpent = System.currentTimeMillis() - timeStart;
            final var estimatedTime = (weeksToScrapCount - weekIndex) * (timeSpent / weekIndex);

            log("Time: %s (%05.2f%%) Est. %s, week (%d/%d): %s".formatted(
                    humanReadableFormat(timeSpent), percentDone, humanReadableFormat(estimatedTime),
                    weekIndex, weeksToScrapCount, calendarView.getCurrentDate()
            ));

            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

            for (final var subject : calendarView.getAvailableSubjects()) {
                subjects.add(this.scrapSubject(calendarView, subject));
            }

            calendarView.goToNextWeek();
        }

        final var groupDto = new GroupDto(
                group,
                semester,
                study,
                ZonedDateTime.now(ZoneId.of("Europe/Warsaw")),
                subjects
        );

        mapper.writeValue(storageFile, groupDto);
        log("Saved to `%s`".formatted(storageFile));

        driver.quit();
    }

    private SubjectDto scrapSubject(final CalendarView calendarView, final String subject) {
        final var subjectPopout = calendarView
                .openSubjectPopout(subject);

        final var dto = subjectPopout
                .toDto();

        subjectPopout
                .close();

        return dto;
    }
}
