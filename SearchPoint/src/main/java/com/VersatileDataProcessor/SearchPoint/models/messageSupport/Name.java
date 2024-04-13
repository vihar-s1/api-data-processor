package com.versatileDataProcessor.searchPoint.models.messageSupport;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@JsonDeserialize(as = Name.class)
public class Name {
    private String title, first, last;
}
