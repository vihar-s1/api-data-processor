package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Document(indexName = "social-media-posts") // to store in elasticsearch-database
public class GenericChannelPost implements Serializable {
    @Id
    private String id;
    private String apiId;
    private ApiType apiType;

    @Setter(AccessLevel.NONE)
    private Map<String, Object> additional;

    @Setter(AccessLevel.NONE)
    private Set<String> tags;

    // Jokes Specific
    private List<String> jokeStatements;

    // Random User Specific
    private User user;

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
