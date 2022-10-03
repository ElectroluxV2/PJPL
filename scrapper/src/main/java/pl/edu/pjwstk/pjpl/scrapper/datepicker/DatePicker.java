package pl.edu.pjwstk.pjpl.scrapper.datepicker;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class DatePicker {
    private static final By activatorBy = By.className("rsDatePickerActivator");
    public static final By datepickerBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar");

    public static OpenedDatePicker open() {
        driver.findElement(activatorBy).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(datepickerBy));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(datepickerBy)));

        return new OpenedDatePicker();
    }
}
