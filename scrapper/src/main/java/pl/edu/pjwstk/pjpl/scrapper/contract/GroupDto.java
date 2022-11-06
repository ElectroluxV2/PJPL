package pl.edu.pjwstk.pjpl.scrapper.contract;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.edu.pjwstk.pjpl.scrapper.JavaScriptDateSerializer;

import java.time.ZonedDateTime;
import java.util.List;

public record GroupDto(
        String name,
        String semester,
        String study,
        @JsonSerialize(using = JavaScriptDateSerializer.class)
        ZonedDateTime updated,
        List<SubjectDto> subjects
) {}
