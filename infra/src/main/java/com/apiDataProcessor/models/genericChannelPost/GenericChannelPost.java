package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import com.apiDataProcessor.models.genericChannelPost.enums.Language;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Document(indexName = "social-media-posts") // to store in elasticsearch-database
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericChannelPost implements Serializable {
    @Id
    private String id;
    private String apiId;
    private ApiType apiType;
    private String authorId;
    private String author;

    private String parentId;
    private String parentName;

    private String title;
    private String body;
    private String htmlBody;
    private Language language;

    private Long totalLikes;
    private Long totalDislikes;

    private Long createdAt;
    private Long lastUpdatedAt;

    @Setter(AccessLevel.NONE)
    private Map<String, Object> additional;

    @Setter(AccessLevel.NONE)
    private Set<String> tags;

    // Jokes Specific
    private List<String> jokeStatements;

    // Random User Specific
    private User user;

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt == null ? null : createdAt.getTime();
    }

    public Timestamp getCreatedAt() {
        return this.createdAt == null ? null : new Timestamp(this.createdAt);
    }

    public void setLastUpdatedAt(Timestamp lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt == null ? null : lastUpdatedAt.getTime();
    }

    public Timestamp getLastUpdatedAt() {
        return this.lastUpdatedAt == null ? null : new Timestamp(this.lastUpdatedAt);
    }

    public void addToAdditional(String key, Object value) {
        if (this.additional == null) {
            this.additional = new HashMap<>();
        }
        this.additional.put(key, value);
    }

    public Object getFromAdditional(String key) {
        if (additional == null) {
            return null;
        }
        return this.additional.getOrDefault(key, null);
    }

    public void addToTags(String tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
    }

    public boolean existTag(String tag) {
        return this.tags != null && this.tags.contains(tag);
    }
}
