package pl.edu.pjwstk.pjpl.scrapper.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.edu.pjwstk.pjpl.scrapper.semester.SemesterSelector;

import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class OpenedStudySelector {
    private static final By valueInputBy = By.name("ctl00$ContentPlaceHolder1$StudiaComboBox");
    private boolean closed = false;

    public List<String> listAvailableStudies() throws RuntimeException {
        if (closed) throw new RuntimeException("Study selector is already closed!");

        return driver
                .findElement(StudySelector.studyDropDownBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getText().trim())
                .toList();
    }

    public void chooseStudy(String studyToChoose) {
        final var dropdown = driver
                .findElement(StudySelector.studyDropDownBy);

        final var currentValue = driver
                .findElement(valueInputBy)
                .getAttribute("value")
                .trim();

        dropdown
                .findElements(By.tagName("li"))
                .stream()
                .filter(li -> li.getText().trim().equalsIgnoreCase(studyToChoose))
                .findFirst()
                .orElseThrow()
                .click();

        if (!currentValue.equalsIgnoreCase(studyToChoose)) {
            // Wait for other options load
            wait.until(ExpectedConditions.stalenessOf(dropdown));
        }

        this.closed = true;
    }
}
