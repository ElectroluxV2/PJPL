package pl.edu.pjwstk.pjpl.scrapper.components.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OpenedStudySelector {
    private static final By valueInputBy = By.name("ctl00$ContentPlaceHolder1$StudiaComboBox");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private boolean closed = false;

    public OpenedStudySelector(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public List<String> listAvailableStudies() throws RuntimeException {
        if (closed) throw new RuntimeException("Study selector is already closed!");

        return driver
                .findElement(StudySelector.studyDropDownBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getAttribute("innerText").trim())
                .filter(name -> !name.isEmpty())
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

        this.listAvailableStudies(); // FIXME: Why is this necessary?

        dropdown
                .findElements(By.tagName("li"))
                .stream()
                .filter(li -> li.getAttribute("innerText").trim().equalsIgnoreCase(studyToChoose))
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
