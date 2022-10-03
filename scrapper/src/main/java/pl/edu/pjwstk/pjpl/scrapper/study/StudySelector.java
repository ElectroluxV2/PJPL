package pl.edu.pjwstk.pjpl.scrapper.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.edu.pjwstk.pjpl.scrapper.semester.OpenedSemesterSelector;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class StudySelector {
    private static final By studyInputBy = By.id("ctl00_ContentPlaceHolder1_StudiaComboBox_Input");
    public static final By studyDropDownBy = By.id("ctl00_ContentPlaceHolder1_StudiaComboBox_DropDown");

    public static OpenedStudySelector open() {
        driver
                .findElement(studyInputBy)
                .click();

        wait.until(ExpectedConditions.presenceOfElementLocated(studyDropDownBy));
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(studyDropDownBy, By.tagName("li"))); // Wait until options load

        return new OpenedStudySelector();
    }
}
