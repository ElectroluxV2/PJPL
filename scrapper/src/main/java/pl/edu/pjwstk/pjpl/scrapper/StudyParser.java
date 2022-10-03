package pl.edu.pjwstk.pjpl.scrapper;

import static pl.edu.pjwstk.pjpl.scrapper.Utils.listComboBoxItems;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.selectComboBoxItem;

public class StudyParser {
    public static void parse() {
        System.out.printf("Getting list of available studies except Gdańsk.%n");
        var studiesToScrap = listComboBoxItems(Constants.STUDY_COMBO_BOX_PARTIAL_ID)
                .stream()
                .filter(study -> study.contains("Gdańsk"))
                .toList();

        for (int studyIndex = 0; studyIndex < studiesToScrap.size(); studyIndex++) {
            final var studyName = studiesToScrap.get(studyIndex);

            System.out.printf("Scrapping study `%s` (%d/%d).%n", studyName, studyIndex + 1, studiesToScrap.size());
            selectComboBoxItem(Constants.STUDY_COMBO_BOX_PARTIAL_ID, studyName);

            GroupParser.parse();
        }
    }
}
