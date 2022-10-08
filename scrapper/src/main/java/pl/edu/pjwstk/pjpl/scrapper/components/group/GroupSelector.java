package pl.edu.pjwstk.pjpl.scrapper.components.group;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class GroupSelector {
    private final static By groupsListBoxBy = By.id("ctl00_ContentPlaceHolder1_GrupyListBox");
    private final static By downloadScheduleButtonBy = By.id("ctl00_ContentPlaceHolder1_PobierzPlanButton_input");
    private final WebDriver driver;
    private final WebDriverWait wait;

    private GroupSelector(final WebDriver driver, final WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static GroupSelector get(final WebDriver driver, final WebDriverWait wait) {
        return new GroupSelector(driver, wait);
    }

    public List<String> listAvailableGroups() {
        return driver
                .findElement(groupsListBoxBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getText().trim())
                .toList();
    }

    public void chooseGroup(final String groupToChoose) {
        this.listAvailableGroups(); // FIXME: Why is this necessary?

        driver
                .findElement(groupsListBoxBy)
                .findElements(By.tagName("li"))
                .stream()
                .filter(item -> item.getText().trim().equalsIgnoreCase(groupToChoose))
                .findFirst()
                .orElseThrow()
                .click();

        final var downloadScheduleButton = driver
                .findElement(downloadScheduleButtonBy);

        downloadScheduleButton.click();

        wait.until(ExpectedConditions.stalenessOf(downloadScheduleButton));
    }
}
