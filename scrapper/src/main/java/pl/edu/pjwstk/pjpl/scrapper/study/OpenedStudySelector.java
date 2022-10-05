package pl.edu.pjwstk.pjpl.scrapper.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    private void close() {
        this.closed = true;
        driver.findElement(By.id("header")).click();

        final var dropdown = driver
                .findElement(StudySelector.studyDropDownBy);

        // Wait for animation
        wait.until(ExpectedConditions.invisibilityOf(dropdown));
    }

    public void chooseStudy(String studyToChoose) {
        if (closed) throw new RuntimeException("Study selector is already closed!");

        final var currentValue = driver
                .findElement(valueInputBy)
                .getAttribute("value")
                .trim();

        if (currentValue.equalsIgnoreCase(studyToChoose)) {
            this.close();
            return;
        }

        final var dropdown = driver
                .findElement(StudySelector.studyDropDownBy);

        dropdown
                .findElements(By.tagName("li"))
                .stream()
                .filter(li -> li.getText().trim().equalsIgnoreCase(studyToChoose))
                .findFirst()
                .ifPresentOrElse(WebElement::click, () -> System.err.printf("Missing study: %s.%n", studyToChoose));

        // Wait for other options load
        wait.until(ExpectedConditions.stalenessOf(dropdown));

        this.close();
    }
}
