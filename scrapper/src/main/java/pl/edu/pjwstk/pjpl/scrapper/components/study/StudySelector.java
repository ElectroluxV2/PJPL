package pl.edu.pjwstk.pjpl.scrapper.components.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StudySelector {
    private static final By studyInputBy = By.id("ctl00_ContentPlaceHolder1_StudiaComboBox_Input");
    public static final By studyDropDownBy = By.id("ctl00_ContentPlaceHolder1_StudiaComboBox_DropDown");
    private final WebDriver driver;
    private final WebDriverWait wait;

    private StudySelector(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static StudySelector get(final WebDriver driver, final WebDriverWait wait) {
        return new StudySelector(driver, wait);
    }

    public OpenedStudySelector open() {
        driver
                .findElement(studyInputBy)
                .click();

        wait.until(ExpectedConditions.presenceOfElementLocated(studyDropDownBy));
        // Waiting until options load here is not solution, hence there may be no options

        return new OpenedStudySelector(driver, wait);
    }
}
