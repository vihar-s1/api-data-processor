package com.versatileDataProcessor.elasticsearchWriter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyResponseBody<T> {
    private String message;
    private Boolean success;
    private T data;
}
