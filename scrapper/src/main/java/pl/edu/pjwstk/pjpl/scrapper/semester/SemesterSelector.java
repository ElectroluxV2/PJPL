package pl.edu.pjwstk.pjpl.scrapper.semester;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.edu.pjwstk.pjpl.scrapper.study.OpenedStudySelector;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class SemesterSelector {
    private static final By semesterInputBy = By.id("ctl00_ContentPlaceHolder1_SemestrComboBox_Input");
    public static final By semesterDropDownBy = By.id("ctl00_ContentPlaceHolder1_SemestrComboBox_DropDown");

    public static OpenedSemesterSelector open() {
        driver
            .findElement(semesterInputBy)
            .click();

        wait.until(ExpectedConditions.presenceOfElementLocated(semesterDropDownBy));
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(semesterDropDownBy, By.tagName("li"))); // Wait until options load

        return new OpenedSemesterSelector();
    }
}
