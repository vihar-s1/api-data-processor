package com.apiDataProcessor.models.apiResponse.twitter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Annotation {
    public static final String START = "start";
    public static final String END = "end";
    public static final String PROBABILITY = "probability";
    public static final String TYPE = "type";
    public static final String NORMALIZED_TEXT = "normalized_text";

    @JsonProperty(START)
    private Long start;

    @JsonProperty(END)
    private Long end;

    @JsonProperty(PROBABILITY)
    private Double probability;

    @JsonProperty(TYPE)
    private String type;

    @JsonProperty(NORMALIZED_TEXT)
    private String normalizedText;
}
