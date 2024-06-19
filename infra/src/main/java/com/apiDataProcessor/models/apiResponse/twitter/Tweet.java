package com.apiDataProcessor.models.apiResponse.twitter;

import com.apiDataProcessor.models.apiResponse.twitter.entities.EditControls;
import com.apiDataProcessor.models.apiResponse.twitter.entities.ReferencedTweet;
import com.apiDataProcessor.models.apiResponse.twitter.entities.TwitterEntities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
    public static final String ID = "id";
    public static final String TEXT = "text";
    public static final String AUTHOR_ID = "author_id";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String LANGUAGE = "lang";
    public static final String CREATED_AT = "created_at";
    public static final String EDIT_HISTORY_TWEEET_IDS = "edit_history_tweet_ids";
    public static final String IN_REPLY_TO_USER_ID = "in_reply_to_user_id";
    public static final String POSSIBLY_SENSITIVE = "possibly_sensitive";
    public static final String ATTACHMENTS = "attachments";
    public static final String EDIT_CONTROLS = "edit_controls";
    public static final String ENTITIES = "entities";
    public static final String REFERENCED_TWEETS = "referenced_tweets";
    public static final String REPLY_SETTINGS = "reply_settings";
    public static final String NON_PUBLIC_METRICS = "non_public_metrics";
    public static final String ORGANIC_METRICS = "organic_metrics";
    public static final String PROMOTED_METRICS = "promoted_metrics";
    public static final String PUBLIC_METRICS = "public_metrics";

    @Id
    @JsonProperty(ID)
    private String id;

    @JsonProperty(TEXT)
    private String text;

    @JsonProperty(AUTHOR_ID)
    private String authorId;

    @JsonProperty(CONVERSATION_ID)
    private String conversationId;

    @JsonProperty(LANGUAGE)
    private String language;

    @JsonProperty(CREATED_AT)
    private Timestamp createdAt;

    @JsonProperty(EDIT_HISTORY_TWEEET_IDS)
    private List<String> editHistoryTweetIDs;

    @JsonProperty(IN_REPLY_TO_USER_ID)
    private String inReplyToUserId;

    @JsonProperty(POSSIBLY_SENSITIVE)
    private Boolean possiblySensitive;

    @JsonProperty(ATTACHMENTS)
    private Map<String, List<String>> attachments;

    @JsonProperty(EDIT_CONTROLS)
    private EditControls editControls;

    @JsonProperty(ENTITIES)
    private TwitterEntities entities;

    @JsonProperty(REFERENCED_TWEETS)
    private List<ReferencedTweet> referencedTweets;

    @JsonProperty(REPLY_SETTINGS)
    private String replySettings;

    @JsonProperty(NON_PUBLIC_METRICS)
    private Map<String, Long> nonPublicMetrics;

    @JsonProperty(ORGANIC_METRICS)
    private Map<String, Long> organicMetrics;

    @JsonProperty(PROMOTED_METRICS)
    private Map<String, Long> promotedMetrics;

    @JsonProperty(PUBLIC_METRICS)
    private Map<String, Long> publicMetrics;
}

