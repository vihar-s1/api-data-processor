package com.apiDataProcessor.models.apiResponse.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditPost implements Serializable {
    private String kind;
    private RedditPostData data;
}
