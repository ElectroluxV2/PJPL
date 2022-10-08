package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import pl.edu.pjwstk.pjpl.scrapper.components.GroupSchedulePage;
import pl.edu.pjwstk.pjpl.scrapper.components.calendarview.CalendarView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static pl.edu.pjwstk.pjpl.scrapper.Utils.humanReadableFormat;
import static pl.edu.pjwstk.pjpl.scrapper.components.calendarview.CalendarView.subjectPopoutBy;


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

    @Override
    public void run() {
        final var driver = SeleniumFactory.makeDriver();
        final var wait = SeleniumFactory.makeWait(driver);
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("PJPL - %s.json".formatted(group)));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }

        try {
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

            final var timeStart = System.currentTimeMillis();
            for (var weekIndex = 1; weekIndex <= weeksToScrapCount; weekIndex++) {
                final var percentDone = (double) weekIndex / weeksToScrapCount * 100;
                final var timeSpent = System.currentTimeMillis() - timeStart;
                final var estimatedTime = (weeksToScrapCount - weekIndex) * (timeSpent / weekIndex);

                System.out.printf("Scrapper [%d] (%s) Time: %s (%05.2f%%) Est. %s, week (%d/%d): %s.%n",
                        id, group, humanReadableFormat(timeSpent), percentDone,
                        humanReadableFormat(estimatedTime), weekIndex, weeksToScrapCount, calendarView.getCurrentDate()
                );

                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

                for (final var subject : calendarView.getAvailableSubjects()) {
                    this.scrapGroup(calendarView, subject, driver, writer);
                }

                calendarView.goToNextWeek();
            }
        } catch (final Exception exception) {
            exception.printStackTrace(System.err);
        } finally {
            driver.quit();
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private void scrapGroup(final CalendarView calendarView, final String subject, final WebDriver driver, final BufferedWriter writer) throws IOException {
        final var subjectPopout = calendarView
                .openSubjectPopout(subject);

        writer.write("-----------\n");
        writer.write(driver.findElement(subjectPopoutBy).getText());
        writer.write("\n-----------\n");

        subjectPopout.close();
    }
}
