package pl.edu.pjwstk.pjpl.scrapper.components.datepicker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthView {
    private static final String optionIdPrefix = "rcMView_";

    private static final By okBy = By.id("rcMView_OK");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public MonthView(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public MonthView selectMonth(int month) {
        final var shotMonthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")).substring(0, 3);
        selectOption(shotMonthName);
        return this;
    }

    public MonthView selectYear(int year) {
        selectOption(String.valueOf(year));
        return this;
    }

    public OpenedDatePicker apply() {
        driver.findElement(okBy).click();
        return new OpenedDatePicker(driver, wait);
    }

    private void selectOption(final String optionIdPostfix) {
        final var optionBy = By.id(optionIdPrefix.concat(optionIdPostfix));
        driver.findElement(optionBy).click();
    }
}
