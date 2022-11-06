package pl.edu.pjwstk.pjpl.scrapper.contract;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.edu.pjwstk.pjpl.scrapper.JavaScriptDateSerializer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@JsonSerialize
public record SubjectDto(
        @JsonSerialize(using = JavaScriptDateSerializer.class)
        ZonedDateTime from,
        @JsonSerialize(using = JavaScriptDateSerializer.class)
        ZonedDateTime to,
        String room,
        String location,
        List<String> groups,
        String color,
        Map<String, String> additionalData
) { }
