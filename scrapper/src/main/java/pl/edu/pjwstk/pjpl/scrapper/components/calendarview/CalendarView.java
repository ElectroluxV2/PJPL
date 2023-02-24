package pl.edu.pjwstk.pjpl.scrapper.components.calendarview;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.components.calendarview.EventPopout.dateBy;

public class CalendarView {
    private static final By nextWeekButtonBy = By.className("rsNextDay");
    private static final By currentDateBy = By.cssSelector(".rsHeader > h2");
    private static final By subjectsBy = By.cssSelector("div[id*=ctl00_ContentPlaceHolder1_PlanZajecRadScheduler].rsAptSubject");
    public static final By subjectPopoutBy = By.id("ctl00_ContentPlaceHolder1_RadToolTipManager1RTMPanel");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public CalendarView(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getCurrentDate() {
        return driver.
                findElement(currentDateBy)
                .getAttribute("innerText")
                .trim();
    }

    public CalendarView goToNextWeek() {

        final var nextWeekButton = driver
                .findElement(nextWeekButtonBy);

        nextWeekButton
                .click();

        wait
                .until(ExpectedConditions.stalenessOf(nextWeekButton));

        return this;
    }

    public List<String> getAvailableSubjects() {
        return driver
                .findElements(subjectsBy)
                .stream()
                .map(subject -> subject.getAttribute("id"))
                .toList();
    }

    public EventPopout openSubjectPopout(final String subjectId) {
        final var subject = driver
                .findElement(By.id(subjectId));

        final var subjectCode = subject
                .getAttribute("innerText")
                .trim()
                .split(" ")[0];

        final var subjectColor = subject
                .getCssValue("background-color");

        subject.click();

        wait
                .withTimeout(Duration.ofSeconds(90)) // Sometimes this site lags as hell
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(subjectPopoutBy, dateBy));

        wait
                .withTimeout(Duration.ofSeconds(90)) // Sometimes this site lags as hell
                .until(ExpectedConditions.textToBePresentInElementLocated(subjectPopoutBy, subjectCode));

        return new EventPopout(driver, wait, subjectColor);
    }
}
