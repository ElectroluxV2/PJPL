package pl.edu.pjwstk.pjpl.scrapper.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.edu.pjwstk.pjpl.scrapper.components.datepicker.DatePicker;
import pl.edu.pjwstk.pjpl.scrapper.components.datepicker.OpenedDatePicker;
import pl.edu.pjwstk.pjpl.scrapper.components.group.GroupSelector;
import pl.edu.pjwstk.pjpl.scrapper.components.semester.OpenedSemesterSelector;
import pl.edu.pjwstk.pjpl.scrapper.components.semester.SemesterSelector;
import pl.edu.pjwstk.pjpl.scrapper.components.study.OpenedStudySelector;
import pl.edu.pjwstk.pjpl.scrapper.components.study.StudySelector;

public class GroupSchedulePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    protected GroupSchedulePage(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;

        driver.get("https://planzajec.pjwstk.edu.pl/PlanGrupy.aspx");
    }

    public static GroupSchedulePage open(final WebDriver driver, final WebDriverWait wait) {
        return new GroupSchedulePage(driver, wait);
    }

    public OpenedSemesterSelector openSemesterSelector() {
        return SemesterSelector.get(driver, wait).open();
    }

    public OpenedStudySelector openStudySelector() {
        return StudySelector.get(driver, wait).open();
    }

    public GroupSelector getGroupSelector() {
        return GroupSelector.get(driver, wait);
    }

    public OpenedDatePicker openDatePicker() {
        return DatePicker.get(driver, wait).open();
    }
}
