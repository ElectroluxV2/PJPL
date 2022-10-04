package pl.edu.pjwstk.pjpl.scrapper.calendarview;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;
import static pl.edu.pjwstk.pjpl.scrapper.calendarview.SubjectPopout.studentsCountBy;

public class CalendarView {
    private static final By nextWeekButtonBy = By.className("rsNextDay");
    private static final By currentDateBy = By.cssSelector(".rsHeader > h2");

    private static final By subjectsBy = By.cssSelector("div[id*=ctl00_ContentPlaceHolder1_PlanZajecRadScheduler].rsAptSubject");

    public static final By subjectPopoutBy = By.id("ctl00_ContentPlaceHolder1_RadToolTipManager1RTMPanel");

    public String getCurrentDate() {
        return driver.
                findElement(currentDateBy)
                .getText()
                .trim();
    }

    public CalendarView goToNetWeek() {

        return this;
    }

    public List<String> getAvailableSubjects() {
        return driver
                .findElements(subjectsBy)
                .stream()
                .map(subject -> subject.getAttribute("id"))
                .toList();
    }

    public SubjectPopout openSubjectPopout(final String subjectId) {
        final var subject = driver
                .findElement(By.id(subjectId));

        subject.click();

        wait
                .withTimeout(Duration.ofSeconds(30)) // Sometimes this site lags as hell
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(subjectPopoutBy, studentsCountBy));

        return new SubjectPopout();
    }
}
