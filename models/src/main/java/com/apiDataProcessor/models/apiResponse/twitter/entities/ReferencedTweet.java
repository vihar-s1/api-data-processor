package com.apiDataProcessor.models.apiResponse.twitter.entities;

import lombok.Data;

@Data
public class ReferencedTweet {
    public static final String TYPE = "type";
    public static final String ID = "id";

    private String type;
    private String id;
}
