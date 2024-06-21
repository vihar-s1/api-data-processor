package com.apiDataProcessor.models.apiResponse.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditApiResponseData implements Serializable {
    public static final String  NEXT_TOKEN = "after";
    public static final String PREV_TOKEN = "before";
    public static final String COUNT = "dist";
    public static final String MODHASH = "modhash";
    public static final String GEO_FILTER = "geo_filter";
    public static final String CHILDREN = "children";

    @JsonProperty(NEXT_TOKEN)
    private String nextToken;

    @JsonProperty(PREV_TOKEN)
    private String prevToken;

    @JsonProperty(COUNT)
    private Long count;

    @JsonProperty(MODHASH)
    private String modhash;

    @JsonProperty(GEO_FILTER)
    private String geoFilter;

    @JsonProperty(CHILDREN)
    private List<RedditPost> children;
}
