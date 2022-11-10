package pl.edu.pjwstk.pjpl.scrapper.components.semester;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OpenedSemesterSelector {
    private static final By valueInputBy = By.name("ctl00$ContentPlaceHolder1$SemestrComboBox");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private boolean closed = false;

    public OpenedSemesterSelector(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public List<String> listAvailableSemesters() throws RuntimeException {
        if (closed) throw new RuntimeException("Semester selector is already closed!");

        return driver
                .findElement(SemesterSelector.semesterDropDownBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getText().trim())
                .filter(name -> !name.isEmpty())
                .toList();
    }

    private void close() {
        this.closed = true;
        driver.findElement(By.id("header")).click();

        final var dropdown = driver
                .findElement(SemesterSelector.semesterDropDownBy);

        // Wait for animation
        wait.until(ExpectedConditions.invisibilityOf(dropdown));
    }

    public void chooseSemester(String semesterToChoose) {
        if (closed) throw new RuntimeException("Semester selector is already closed!");

        final var currentValue = driver
                .findElement(valueInputBy)
                .getAttribute("value")
                .trim();

        if (semesterToChoose.equalsIgnoreCase(currentValue)) {
            this.close();
            return;
        }

        final var dropdown = driver
                .findElement(SemesterSelector.semesterDropDownBy);

        this.listAvailableSemesters(); // FIXME: Why is this necessary?

        dropdown
                .findElements(By.tagName("li"))
                .stream()
                .filter(li -> li.getText().trim().equalsIgnoreCase(semesterToChoose))
                .findFirst()
                .orElseThrow()
                .click();

        // Wait for other options load
        wait
                .withTimeout(Duration.ofSeconds(90))
                .until(ExpectedConditions.stalenessOf(dropdown));

        this.close();
    }
}
