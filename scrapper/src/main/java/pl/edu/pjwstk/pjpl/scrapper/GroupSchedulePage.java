package pl.edu.pjwstk.pjpl.scrapper;

import pl.edu.pjwstk.pjpl.scrapper.datepicker.DatePicker;
import pl.edu.pjwstk.pjpl.scrapper.datepicker.OpenedDatePicker;
import pl.edu.pjwstk.pjpl.scrapper.group.GroupSelector;
import pl.edu.pjwstk.pjpl.scrapper.semester.OpenedSemesterSelector;
import pl.edu.pjwstk.pjpl.scrapper.semester.SemesterSelector;
import pl.edu.pjwstk.pjpl.scrapper.study.OpenedStudySelector;
import pl.edu.pjwstk.pjpl.scrapper.study.StudySelector;

import static pl.edu.pjwstk.pjpl.scrapper.Main.driver;

public class GroupSchedulePage {

    public static GroupSchedulePage open() {
        driver.get(Constants.MAIN_URL);

        return new GroupSchedulePage();
    }

    public OpenedSemesterSelector openSemesterSelector() {
        return SemesterSelector.open();
    }

    public OpenedStudySelector openStudySelector() {
        return StudySelector.open();
    }

    public GroupSelector getGroupSelector() {
        return new GroupSelector();
    }

    public OpenedDatePicker openDatePicker() {
        return DatePicker.open();
    }
}
