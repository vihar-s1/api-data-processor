package com.apiDataProcessor.models.apiResponse.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class
RedditPostData implements Serializable {
    public static final String ID = "id";
    public static final String SUBREDDIT = "subreddit";
    public static final String SUBREDDIT_TYPE = "subreddit_type";
    public static final String SUBREDDIT_ID = "subreddit_id";
    public static final String SUBREDDIT_NAME_PREFIXED = "subreddit_name_prefixed";
    public static final String DOMAIN = "domain";
    public static final String TITLE = "title";
    public static final String SELFTEXT = "selftext";
    public static final String SELFTEXT_HTML = "selftext_html";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_FULLNAME = "author_fullname";
    public static final String NAME = "name";
    public static final String UPVOTE_RATIO = "upvote_ratio";
    public static final String UPS = "ups";
    public static final String DOWNS = "downs";
    public static final String TOTAL_AWARDS_RECEIVED = "total_awards_received";
    public static final String TOP_AWARD_TYPE = "top_award_type";
    public static final String NUMBER_OF_COMMENTS = "num_comments";
    public static final String SCORE = "score";
    public static final String OVER_18 = "over_18";
    public static final String CREATED = "created";
    public static final String CREATED_UTC = "created_utc";
    public static final String APPROVED_AT_UTC = "approved_at_utc";
    public static final String PERMA_LINK = "permalink";
    public static final String URL = "url";
    public static final String MEDIA = "media";
    public static final String IS_VIDEO = "is_video";
    public static final String MEDIA_ONLY = "media_only";

    @Id
    @JsonProperty(ID)
    private String id;

    @JsonProperty(SUBREDDIT)
    private String subreddit;

    @JsonProperty(SUBREDDIT_TYPE)
    private String subredditType;

    @JsonProperty(SUBREDDIT_ID)
    private String subredditId;

    @JsonProperty(SUBREDDIT_NAME_PREFIXED)
    private String subredditNamePrefixed;

    @JsonProperty(DOMAIN)
    private String domain;

    @JsonProperty(TITLE)
    private String title;

    @JsonProperty(SELFTEXT)
    private String selftext;

    @JsonProperty(SELFTEXT_HTML)
    private String selftextHtml;

    @JsonProperty(AUTHOR)
    private String author;

    @JsonProperty(AUTHOR_FULLNAME)
    private String author_fullname;

    @JsonProperty(NAME)
    private String name;

    @JsonProperty(UPVOTE_RATIO)
    private Double upvoteRatio;

    @JsonProperty(UPS)
    private Long ups;

    @JsonProperty(DOWNS)
    private Long downs;

    @JsonProperty(TOTAL_AWARDS_RECEIVED)
    private Long totalAwardsReceived;

    @JsonProperty(TOP_AWARD_TYPE)
    private String topAwardType;

    @JsonProperty(NUMBER_OF_COMMENTS)
    private Long numberOfComments;

    @JsonProperty(SCORE)
    private Long score;

    @JsonProperty(OVER_18)
    private Boolean over18;

    @JsonProperty(CREATED)
    private Double created;

    @JsonProperty(CREATED_UTC)
    private Long createdUTC;

    @JsonProperty(APPROVED_AT_UTC)
    private String approvedAtUTC;

    @JsonProperty(PERMA_LINK)
    private String permalink;

    @JsonProperty(URL)
    private String url;

    @JsonProperty(MEDIA)
    private Object media;

    @JsonProperty(IS_VIDEO)
    private Boolean isVideo;

    @JsonProperty(MEDIA_ONLY)
    private Boolean mediaOnly;
}
