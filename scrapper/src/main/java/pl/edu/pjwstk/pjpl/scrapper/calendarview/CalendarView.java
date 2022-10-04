package pl.edu.pjwstk.pjpl.scrapper.calendarview;

import org.openqa.selenium.By;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;

public class CalendarView {
    private final By nextWeekButtonBy = By.className("rsNextDay");
    private final By currentDateBy = By.cssSelector(".rsHeader > h2");

    public void printCurrentDate() {
        final var currentDate = driver.
                findElement(currentDateBy)
                .getText()
                .trim();

        System.out.println(currentDate);
    }

    public CalendarView goToNetWeek() {

        return this;
    }
}
