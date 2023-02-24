package pl.edu.pjwstk.pjpl.scrapper.components.datepicker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.edu.pjwstk.pjpl.scrapper.components.calendarview.CalendarView;

import java.time.Duration;

public class OpenedDatePicker {
    private static final By titleBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_Title");
    private static final By monthViewBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_FastNavPopup");
    private static final By dayTableBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_Top");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public OpenedDatePicker(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    public MonthView openMonthView() {
        wait.until(ExpectedConditions.elementToBeClickable(titleBy));
        driver.findElement(titleBy).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(monthViewBy));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(monthViewBy)));

        return new MonthView(driver, wait);
    }

    public CalendarView chooseFirstAvailableDay() {
        driver
                .findElement(dayTableBy)
                .findElements(By.tagName("td"))
                .stream()
                .filter(td -> !td.getAttribute("innerText").trim().isEmpty())
                .findFirst()
                .orElseThrow()
                .click();

        wait
                .withTimeout(Duration.ofSeconds(90)) // Sometimes this site lags as hell
                .until(ExpectedConditions.invisibilityOf(driver.findElement(DatePicker.datepickerBy)));

        return new CalendarView(driver, wait);
    }
}
