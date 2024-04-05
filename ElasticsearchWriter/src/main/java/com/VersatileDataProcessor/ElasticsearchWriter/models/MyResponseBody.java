package com.versatileDataProcessor.elasticsearchWriter.models;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class MyResponseBody<T> {
    private String message;
    private Boolean success;
    private T data;
}
