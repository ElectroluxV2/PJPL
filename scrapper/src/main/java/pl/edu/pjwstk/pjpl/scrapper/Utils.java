package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;

public class Utils {
    public static List<String> listComboBoxItems(final String partialId) {
        final var comboBoxBy = By.id("%s_Input".formatted(partialId));
        final var comboBoxDropDownBy = By.id("%s_DropDown".formatted(partialId));
        openComboBox(comboBoxBy, comboBoxDropDownBy);

        var dropDownElement = driver.findElement(comboBoxDropDownBy);
        var availableItems = dropDownElement.findElements(By.tagName("li"));

        System.out.printf("Listing all available options for `%s`:%n", partialId);
        availableItems.forEach(item -> System.out.printf("- `%s`%n", item.getText()));

        return availableItems.stream().map(i -> i.getText().trim()).toList();
    }

    /**
     * @Notice: This method does not wait for click result
     */
    public static void selectComboBoxItem(final String partialId, final String itemText) {
        final var comboBoxBy = By.id("%s_Input".formatted(partialId));
        final var comboBoxDropDownBy = By.id("%s_DropDown".formatted(partialId));

        System.out.printf("Selecting `%s` ComboBox item.%n", itemText);
        openComboBox(comboBoxBy, comboBoxDropDownBy);

        final var comboBoxDropDownElement = driver.findElement(comboBoxDropDownBy);
        final var items = comboBoxDropDownElement.findElements(By.tagName("li"));
        // noinspection ResultOfMethodCallIgnored
        items.stream().map(WebElement::getText).toList(); // Without this, below line won't work for non ascii characters
        final var foundItem = items.stream().filter(item -> item.getText().trim().equalsIgnoreCase(itemText)).findFirst().orElseThrow();

        foundItem.click();

        wait.until(ExpectedConditions.invisibilityOf(comboBoxDropDownElement)); // Close animation
    }

    public static void openComboBox(final By comboBoxBy, final By comboBoxDropDownBy) {
        final var comboBoxItemsBy = By.tagName("li");
        final var comboBoxElement = driver.findElement(comboBoxBy);

        wait.until(ExpectedConditions.elementToBeClickable(comboBoxElement));
        comboBoxElement.click(); // There is loading animation
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(comboBoxDropDownBy, comboBoxItemsBy));
    }
}
