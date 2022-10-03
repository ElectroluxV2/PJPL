package pl.edu.pjwstk.pjpl.scrapper.group;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class GroupSelector {
    private final static By groupsListBoxBy = By.id("ctl00_ContentPlaceHolder1_GrupyListBox");
    private final static By downloadScheduleButtonBy = By.id("ctl00_ContentPlaceHolder1_PobierzPlanButton_input");

    public List<String> listAvailableGroups() {
        return driver
                .findElement(groupsListBoxBy)
                .findElements(By.tagName("li"))
                .stream()
                .map(li -> li.getText().trim())
                .toList();
    }

    public void chooseGroup(final String groupToChoose) {
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
