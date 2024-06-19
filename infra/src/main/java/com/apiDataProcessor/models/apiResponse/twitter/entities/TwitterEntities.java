package com.apiDataProcessor.models.apiResponse.twitter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TwitterEntities {
    public static final String ANNOTATIONS = "annotations";
    public static final String CASHTAGS = "cashtags";
    public static final String HASHTAGS = "hashtags";
    public static final String MENTIONS = "mentions";
    public static final String URLS = "urls";

    @JsonProperty(ANNOTATIONS)
    private List<Annotation> annotations;

    @JsonProperty(CASHTAGS)
    private List<TwitterTag> cashtags;

    @JsonProperty(HASHTAGS)
    private List<TwitterTag> hashtags;

    @JsonProperty(MENTIONS)
    private List<TwitterTag> mentions;

    @JsonProperty(URLS)
    private List<TwitterUrl> urls;
}
