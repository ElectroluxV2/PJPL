package pl.edu.pjwstk.pjpl.scrapper.datepicker;

import org.openqa.selenium.By;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;

public class MonthView {
    private static final String optionIdPrefix = "rcMView_";

    private static final By okBy = By.id("rcMView_OK");

    public MonthView selectMonth(int month) {
        final var shotMonthName = Month.of(month).getDisplayName(TextStyle.FULL, new Locale("pl")).substring(0, 3);
        selectOption(shotMonthName);
        return this;
    }

    public MonthView selectYear(int year) {
        selectOption(String.valueOf(year));
        return this;
    }

    public OpenedDatePicker apply() {
        driver.findElement(okBy).click();
        return new OpenedDatePicker();
    }

    private void selectOption(final String optionIdPostfix) {
        final var optionBy = By.id(optionIdPrefix.concat(optionIdPostfix));
        driver.findElement(optionBy).click();
    }
}
