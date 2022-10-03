package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.By;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;

public class CalendarParser {
    public static void parse() {
        // Wait for load
        final var previousDayButtonBy = By.className("rsPrevDay");
        final var nextDayButtonBy = By.className("rsNextDay");
        final var todayButtonBy = By.className("rsToday");
        final var currentDateBy = By.cssSelector(".rsHeader > h2");
        final var previousDayButton = driver.findElement(previousDayButtonBy);
        final var nextDayButton = driver.findElement(nextDayButtonBy);
        final var todayButton = driver.findElement(todayButtonBy);
        final var currentDate = driver.findElement(currentDateBy);

        System.out.println(currentDate.getText().trim());

        // Start parsing today

        // Parse backward 6 months

        // Parse forward 6 months
    }
}
