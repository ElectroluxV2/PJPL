package pl.edu.pjwstk.pjpl.scrapper;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;
import static pl.edu.pjwstk.pjpl.scrapper.Main.wait;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.listComboBoxItems;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.selectComboBoxItem;

public class SemesterParser {
    public static void parse() {
        driver.get(Constants.MAIN_URL);
        System.out.println("Getting list of available semesters.");
        var semestersToScrap = listComboBoxItems(Constants.SEMESTER_COMBO_BOX_PARTIAL_ID);

        System.out.println("Scrapping semester one by one.");
        for (int semesterIndex = 0; semesterIndex < semestersToScrap.size(); semesterIndex++) {
            final var semesterName = semestersToScrap.get(semesterIndex);

            System.out.printf("Scrapping semester `%s` (%d/%d).%n", semesterName, semesterIndex + 1, semestersToScrap.size());
            selectComboBoxItem(Constants.SEMESTER_COMBO_BOX_PARTIAL_ID, semesterName);

            StudyParser.parse();
        }
    }
}
