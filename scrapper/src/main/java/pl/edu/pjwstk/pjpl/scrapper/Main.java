package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static pl.edu.pjwstk.pjpl.scrapper.Utils.listComboBoxItems;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.selectComboBoxItem;

public class Main {
    private static final WebDriver driver = new ChromeDriver();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(6000));
    public static void main(final String[] args) throws InterruptedException {
        System.out.println("Welcome to PJPL Scrapper!");

        loadMainPage();

        System.out.println("Getting list of available semesters.");
        var semestersToScrap = listComboBoxItems(driver, wait, Constants.SEMESTER_COMBO_BOX_PARTIAL_ID);

        System.out.println("Scrapping semester one by one.");
        for (int semesterIndex = 0; semesterIndex < semestersToScrap.size(); semesterIndex++) {
            final var semesterName = semestersToScrap.get(semesterIndex);

            System.out.printf("Scrapping semester `%s` (%d/%d).%n", semesterName, semesterIndex + 1, semestersToScrap.size());
            selectComboBoxItem(driver, wait, Constants.SEMESTER_COMBO_BOX_PARTIAL_ID, semesterName);

            System.out.printf("Getting list of available studies for semester `%s`.%n", semesterName);
            var studiesToScrap = listComboBoxItems(driver, wait, Constants.STUDY_COMBO_BOX_PARTIAL_ID);

            for (int studyIndex = 0; studyIndex < studiesToScrap.size(); studyIndex++) {
                final var studyName = studiesToScrap.get(studyIndex);

                System.out.printf("Scrapping study `%s` (%d/%d).%n", studyName, studyIndex + 1, studiesToScrap.size());
                selectComboBoxItem(driver, wait, Constants.STUDY_COMBO_BOX_PARTIAL_ID, studyName);

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

                    // Wait for load
                    final var previousDayButtonBy = By.className("rsPrevDay");
                    final var nextDayButtonBy = By.className("rsNextDay");
                    final var todayButtonBy = By.className("rsToday");
                    final var currentDateBy = By.cssSelector(".rsHeader > h2");

                    wait.until(ExpectedConditions.stalenessOf(showPlanButton));

                    final var previousDayButton = driver.findElement(previousDayButtonBy);
                    final var nextDayButton = driver.findElement(nextDayButtonBy);
                    final var todayButton = driver.findElement(todayButtonBy);
                    final var currentDate = driver.findElement(currentDateBy);

                    System.out.println(currentDate.getText().trim());

                    // Start parsing today

                    // Parse backward 6 months

                    // Parse forward 6 months
                }
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofNanos(2000));
        driver.quit();
    }

    public static void loadMainPage() {
        driver.get("https://planzajec.pjwstk.edu.pl/PlanGrupy.aspx");
    }
}
