package com.versatileDataProcessor.searchPoint.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import com.versatileDataProcessor.searchPoint.models.messageSupport.ID;
import com.versatileDataProcessor.searchPoint.models.messageSupport.Name;
import com.versatileDataProcessor.searchPoint.serializers.StandardMessageSerializer;
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

@Data
@Document(indexName = "api-data")
@TypeAlias("StandardMessage")
@JsonSerialize(using = StandardMessageSerializer.class)
//@JsonInclude(JsonInclude.Include.NON_NULL) // Equivalent of using the serializer to omit the null valued fields
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

    public void addAdditional(String key, String value){
        if (this.additionalData == null){
            this.additionalData = new HashMap<>();
        }
        if (key != null && value != null) {
            additionalData.put(key, value);
        }
    }

    public String getAdditional(String key) {
        return additionalData.getOrDefault(key, null);
    }
}
