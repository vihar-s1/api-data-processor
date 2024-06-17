package com.apiDataProcessor.models.apiResponse.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterResponseAdditional {
    public static final String MEDIA = "media";
    @JsonProperty(MEDIA)
    private List<Media> media;
}
