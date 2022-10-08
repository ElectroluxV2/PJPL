package pl.edu.pjwstk.pjpl.scrapper.components.semester;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SemesterSelector {
    private static final By semesterInputBy = By.id("ctl00_ContentPlaceHolder1_SemestrComboBox_Input");
    public static final By semesterDropDownBy = By.id("ctl00_ContentPlaceHolder1_SemestrComboBox_DropDown");
    private final WebDriver driver;
    private final WebDriverWait wait;

    private SemesterSelector(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static SemesterSelector get(final WebDriver driver, final WebDriverWait wait) {
        return new SemesterSelector(driver, wait);
    }

    public OpenedSemesterSelector open() {
        driver
                .findElement(semesterInputBy)
                .click();

        wait.until(ExpectedConditions.presenceOfElementLocated(semesterDropDownBy));
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(semesterDropDownBy, By.tagName("li"))); // Wait until options load

        return new OpenedSemesterSelector(driver, wait);
    }
}
