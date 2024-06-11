package com.VersatileDataProcessor.ElasticsearchWriter.models.messageSupport.randomUser;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@JsonDeserialize(as = ID.class)
public class ID {
    private String name, value;
}
