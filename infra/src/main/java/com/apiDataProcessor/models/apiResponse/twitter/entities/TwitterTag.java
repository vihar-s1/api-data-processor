package com.apiDataProcessor.models.apiResponse.twitter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwitterTag {
    public static final String START = "start";
    public static final String END = "end";
    public static final String TAG = "tag";

    @JsonProperty(START)
    private Long start;
    @JsonProperty(END)
    private Long end;
    @JsonProperty(TAG)
    private String tag;
}
