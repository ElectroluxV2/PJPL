package pl.edu.pjwstk.pjpl.scrapper.contract;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@JsonSerialize
public record SubjectDto(
        ZonedDateTime from,
        ZonedDateTime to,
        String room,
        String location,
        List<String> groups,
        String color,
        Map<String, String> additionalData
) { }
