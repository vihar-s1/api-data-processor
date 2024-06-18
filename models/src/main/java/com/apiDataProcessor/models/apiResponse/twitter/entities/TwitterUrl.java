package com.apiDataProcessor.models.apiResponse.twitter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwitterUrl {
    public static final String URL = "url";
    public static final String DISPLAY_URL = "display_url";
    public static final String EXPANDED_URL = "expanded_url";
    public static final String UNWOUNDED_URL = "unwounded_url";
    public static final String START = "start";
    public static final String END = "end";
    public static final String STATUS = "status";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String MEDIA_KEY = "media_key";

    @JsonProperty(URL)
    private String url;

    @JsonProperty(DISPLAY_URL)
    private String displayUrl;

    @JsonProperty(EXPANDED_URL)
    private String expandedUrl;

    @JsonProperty(UNWOUNDED_URL)
    private String unwoundedUrl;

    @JsonProperty(START)
    private Long start;

    @JsonProperty(END)
    private Long end;

    @JsonProperty(STATUS)
    private String status;

    @JsonProperty(TITLE)
    private String title;

    @JsonProperty(DESCRIPTION)
    private String description;

    @JsonProperty(MEDIA_KEY)
    private String mediaKey;
}
