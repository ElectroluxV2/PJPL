package pl.edu.pjwstk.pjpl.scrapper.components.calendarview;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.edu.pjwstk.pjpl.scrapper.contract.SubjectDto;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.removeLastChars;

public class EventPopout {
    public static final String[] knownProperties = {
            "Grupy", "Budynek", "Sala", "DataZajec", "GodzRozp", "GodzZakon", "CzasTrwania"
    };
    public static final By groupsBy = By.cssSelector("span[id*=Grupy]");
    public static final By locationBy = By.cssSelector("span[id*=Budynek]");
    public static final By roomBy = By.cssSelector("span[id*=Sala]");
    public static final By dateBy = By.cssSelector("span[id*=DataZajec]");
    public static final By timeFromBy = By.cssSelector("span[id*=GodzRozp]");
    public static final By timeToBy = By.cssSelector("span[id*=GodzZakon]");
    public static final By durationBy = By.cssSelector("span[id*=CzasTrwania]");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String color;

    public EventPopout(final WebDriver driver, final WebDriverWait wait, final String color) {
        this.driver = driver;
        this.wait = wait;
        this.color = color;
    }

    public SubjectDto toDto() {
        return new SubjectDto(
            getFrom(),
            getTo(),
            getRoom(),
            getLocation(),
            getGroups(),
            color,
            getAdditionalData()
        );
    }

    private Map<String, String> getAdditionalData() {
        final var allProperties = getPopout()
                .findElements(By.cssSelector("table>tbody>tr>td>span[id], table>tbody>tr>td>b"));

        final var counter = new AtomicInteger();

        return allProperties
                .stream()
                .collect(groupingBy(x -> counter.getAndIncrement() / 2))
                .values()
                .stream()
                .filter(list -> Arrays.stream(knownProperties).noneMatch(property -> list.get(1).getAttribute("id").contains(property)))
                .filter(list -> !list.get(1).getText().trim().isEmpty())
                .collect(Collectors.toMap(list -> removeLastChars(list.get(0).getText().trim(), 1), list -> list.get(1).getText().trim()));
    }

    public Duration getDuration() {
        final var minutes = Long.parseLong(getTrimmedText(durationBy).trim().split(" ")[0]);
        return Duration.ofMinutes(minutes);
    }

    public ZonedDateTime getTo() {
        final var date = getDate(getTrimmedText(dateBy));
        final var time = getTime(getTrimmedText(timeToBy));

        return ZonedDateTime.of(date, time, ZoneId.of("Europe/Warsaw"));
    }

    public ZonedDateTime getFrom() {
        final var date = getDate(getTrimmedText(dateBy));
        final var time = getTime(getTrimmedText(timeFromBy));

        return ZonedDateTime.of(date, time, ZoneId.of("Europe/Warsaw"));
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

    public List<String> getGroups() {
        return getStringList(getTrimmedText(groupsBy));
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
}
