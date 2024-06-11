package com.VersatileDataProcessor.Models.standardMediaData;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.apiResponse.randomUser.User;
import lombok.*;

import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class StandardMediaData {
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
