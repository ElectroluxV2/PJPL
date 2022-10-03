package pl.edu.pjwstk.pjpl.scrapper.semester;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class OpenedSemesterSelector {
    private static final By valueInputBy = By.name("ctl00$ContentPlaceHolder1$SemestrComboBox");
    private boolean closed = false;

    public List<String> listAvailableSemesters() throws RuntimeException {
        if (closed) throw new RuntimeException("Semester selector is already closed!");

        return driver
                .findElement(SemesterSelector.semesterDropDownBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getText().trim())
                .toList();
    }

    public void chooseSemester(String semesterToChoose) {
        final var currentValue = driver
                .findElement(valueInputBy)
                .getAttribute("value")
                .trim();

        final var dropdown = driver
                .findElement(SemesterSelector.semesterDropDownBy);

        dropdown
                .findElements(By.tagName("li"))
                .stream()
                .filter(li -> li.getText().trim().equalsIgnoreCase(semesterToChoose))
                .findFirst()
                .orElseThrow()
                .click();

        if (!currentValue.equalsIgnoreCase(semesterToChoose)) {
            // Wait for other options load
            wait.until(ExpectedConditions.stalenessOf(dropdown));
        }

        this.closed = true;
    }
}
