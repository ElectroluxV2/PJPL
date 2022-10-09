package pl.edu.pjwstk.pjpl.scrapper.contract;

import java.time.ZonedDateTime;
import java.util.List;

public record GroupDto(
        String name,
        String semester,
        String study,
        ZonedDateTime updated,
        List<SubjectDto> subjects
) {}
