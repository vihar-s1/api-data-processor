package com.VersatileDataProcessor.Models.apiResponse.joke;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Joke implements Serializable {
    private String category;
    private String type;
    private String joke;
    private Map<String, Boolean> flags;
}
