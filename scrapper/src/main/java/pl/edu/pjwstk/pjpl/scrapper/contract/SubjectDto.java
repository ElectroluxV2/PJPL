package pl.edu.pjwstk.pjpl.scrapper.contract;

import pl.edu.pjwstk.pjpl.scrapper.components.calendarview.SubjectPopout;

import java.util.List;

public record SubjectDto(
        SubjectPopout.StudentCount studentCount,
        String subjectCode,
        String subjectType,
        List<String> groups,
        List<String> lecturers
) { }
