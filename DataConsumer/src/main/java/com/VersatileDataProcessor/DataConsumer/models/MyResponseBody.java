package com.versatileDataProcessor.dataConsumer.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyResponseBody<T> {
    private String message;
    private Boolean success;
    private T data;
}
