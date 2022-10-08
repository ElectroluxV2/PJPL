package pl.edu.pjwstk.pjpl.scrapper.components.datepicker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DatePicker {
    private static final By activatorBy = By.className("rsDatePickerActivator");
    public static final By datepickerBy = By.id("ctl00_ContentPlaceHolder1_PlanZajecRadScheduler_SelectedDateCalendar");
    private final WebDriver driver;
    private final WebDriverWait wait;

    private DatePicker(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static DatePicker get(final WebDriver driver, final WebDriverWait wait) {
        return new DatePicker(driver, wait);
    }

    public OpenedDatePicker open() {
        driver.findElement(activatorBy).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(datepickerBy));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(datepickerBy)));

        return new OpenedDatePicker(driver, wait);
    }
}
