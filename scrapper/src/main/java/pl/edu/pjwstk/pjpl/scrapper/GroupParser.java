package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class GroupParser {
    public static void parse() {
        // We need to wait until groups load
        final var groupsListBoxBy = By.id(Constants.GROUPS_LIST_BOX_ID);
        final var groupsListBox = driver.findElement(groupsListBoxBy);

        wait.until(ExpectedConditions.elementToBeClickable(groupsListBox.findElement(By.tagName("li"))));

        final var groupsToScrap = groupsListBox.findElements(By.tagName("li")).stream().map(item -> item.getText().trim()).toList();
        for (int groupIndex = 0; groupIndex < groupsToScrap.size(); groupIndex++) {
            final var groupName = groupsToScrap.get(groupIndex);
            final var groups = driver.findElement(groupsListBoxBy).findElements(By.tagName("li"));

            System.out.printf("Scrapping group `%s` (%d/%d).%n", groupName, groupIndex + 1, groupsToScrap.size());

            // noinspection ResultOfMethodCallIgnored
            groups.stream().map(WebElement::getText).toList();// Without this, below line won't work for non ascii characters
            groups.stream().filter(group -> group.getText().trim().equalsIgnoreCase(groupName)).findFirst().orElseThrow().click();

            wait.until(ExpectedConditions.elementToBeClickable(By.id(Constants.SHOW_PLAN_BUTTON_ID)));
            final var showPlanButton = driver.findElement(By.id(Constants.SHOW_PLAN_BUTTON_ID));
            showPlanButton.click();
            wait.withTimeout(Duration.ofMinutes(1)).until(ExpectedConditions.stalenessOf(showPlanButton));

            CalendarParser.parse();
        }
    }
}
