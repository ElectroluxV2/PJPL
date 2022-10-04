package pl.edu.pjwstk.pjpl.scrapper.datepicker;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.edu.pjwstk.pjpl.scrapper.calendarview.CalendarView;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class OpenedDatePicker {
    private static final By titleBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_Title");

    private static final By monthViewBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_FastNavPopup");

    private static final By dayTableBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar_Top");
    public MonthView openMonthView() {
        driver.findElement(titleBy).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(monthViewBy));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(monthViewBy)));

        return new MonthView();
    }

    public CalendarView chooseFirstAvailableDay() {
        driver
                .findElement(dayTableBy)
                .findElements(By.tagName("td"))
                .stream()
                .filter(td -> !td.getText().trim().isEmpty())
                .findFirst()
                .orElseThrow()
                .click();

        wait
                .until(ExpectedConditions.invisibilityOf(driver.findElement(DatePicker.datepickerBy)));

        return new CalendarView();
    }
}
