package com.VersatileDataProcessor.RegexManager.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyResponseBody<T> {
    private String message;
    private Boolean success;
    private T data;

    public MyResponseBody() {
    }

    public MyResponseBody(String message, Boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }
}
