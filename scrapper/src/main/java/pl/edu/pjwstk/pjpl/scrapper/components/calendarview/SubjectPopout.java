package pl.edu.pjwstk.pjpl.scrapper.components.calendarview;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class SubjectPopout {
    public static final By studentsCountBy = By.cssSelector("span[id*=LiczbaStudentow]");
    public static final By subjectCodeBy = By.cssSelector("span[id*=KodPrzedmiotu]");
    public static final By subjectTypeBy = By.cssSelector("span[id*=TypZajec]");
    public static final By groupsBy = By.cssSelector("span[id*=Grupy]");
    public static final By lecturersBy = By.cssSelector("span[id*=Dydaktycy]");
    public static final By locationBy = By.cssSelector("span[id*=Budynek]");
    public static final By roomBy = By.cssSelector("span[id*=Sala]");
    public static final By dateBy = By.cssSelector("span[id*=DataZajec]");
    public static final By timeFromBy = By.cssSelector("span[id*=GodzRozp]");
    public static final By timeToBy = By.cssSelector("span[id*=GodzZakon]");
    public static final By durationBy = By.cssSelector("span[id*=CzasTrwania]");
    public static final By teamsCodeBy = By.cssSelector("span[id*=KodMsTeams]");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SubjectPopout(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getTeamsCode() {
        return getTrimmedText(teamsCodeBy);
    }

    public Duration getDuration() {
        final var minutes = Long.parseLong(getTrimmedText(durationBy).trim().split(" ")[0]);
        return Duration.ofMinutes(minutes);
    }

    public ZonedDateTime getTo() {
        final var date = getDate(getTrimmedText(dateBy));
        final var time = getTime(getTrimmedText(timeToBy));

        return ZonedDateTime.of(date, time, ZoneOffset.UTC/*ZoneId.of("Europe/Warsaw")*/);
    }

    public ZonedDateTime getFrom() {
        final var date = getDate(getTrimmedText(dateBy));
        final var time = getTime(getTrimmedText(timeFromBy));

        return ZonedDateTime.of(date, time, ZoneOffset.UTC/*ZoneId.of("Europe/Warsaw")*/);
    }

    private LocalTime getTime(final String time) {
        final var parts = Arrays
                .stream(time.split(":"))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();

        return LocalTime.of(parts[0], parts[1], parts[2]);
    }

    private LocalDate getDate(final String date) {
        final var parts = Arrays
                .stream(date.split("\\."))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();

        return LocalDate.of(parts[2], parts[1], parts[0]);
    }

    public String getRoom() {
        return getTrimmedText(roomBy);
    }

    public String getLocation() {
        return getTrimmedText(locationBy);
    }

    public List<String> getLectures() {
        return getStringList(getTrimmedText(lecturersBy));
    }

    public List<String> getGroups() {
        return getStringList(getTrimmedText(groupsBy));
    }

    public String getSubjectType() {
        return getTrimmedText(subjectTypeBy);
    }

    public String getSubjectCode() {
        return getTrimmedText(subjectCodeBy);
    }

    public StudentCount getStudentCount() {
        return new StudentCount(getTrimmedText(studentsCountBy));
    }

    private WebElement getPopout() {
        return driver.findElement(CalendarView.subjectPopoutBy);
    }

    private String getTrimmedText(final By selector) {
        return getPopout()
                .findElement(selector)
                .getText()
                .trim();
    }

    private List<String> getStringList(final String combined) {
        return List.of(combined.split(","));
    }

    public void close() {
        driver.findElement(By.id("header")).click();
    }

    public static class StudentCount {
        private final int total;
        private final int itn;

        public StudentCount(final String input) {
            final var parts = input.split(" ");
            total = Integer.parseInt(parts[0]);
            itn = Integer.parseInt(parts[1]);
        }

        public int getTotal() {
            return total;
        }

        public int getItn() {
            return itn;
        }
    }
}
