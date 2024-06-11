package com.VersatileDataProcessor.ElasticsearchWriter.models.standardMessage;

import com.VersatileDataProcessor.ElasticsearchWriter.models.MessageType;
import com.VersatileDataProcessor.ElasticsearchWriter.models.messageSupport.randomUser.ID;
import com.VersatileDataProcessor.ElasticsearchWriter.models.messageSupport.randomUser.Name;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data @AllArgsConstructor
@Document(indexName = "api-data")
@TypeAlias("StandardMessage")
public class StandardMessage implements Serializable {
    private String id;
    private MessageType messageType;
    // All String fields
    @Setter(AccessLevel.NONE)
    private Map<String, String> additionalData;

    // Joke Fields
    private Set<String> tags;
    private List<String> jokeStatements;

    // RandomUser Fields
    private Name name;
    private ID userId;

    public StandardMessage() {
        this.additionalData = new HashMap<>();
    }

    public void addAdditional(String key, String value){
        if (key != null && value != null) {
            additionalData.put(key, value);
        }
    }

    public String getAdditional(String key) {
        return additionalData.getOrDefault(key, null);
    }
}
