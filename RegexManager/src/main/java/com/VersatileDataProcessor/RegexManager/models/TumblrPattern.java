package com.VersatileDataProcessor.RegexManager.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("TumblrPatterns")
public class TumblrPattern {

    public static int MAX_EXPRESSION_LENGTH = 511;
    @Id
    private String _id;
    private String expression;
    public TumblrPattern() {}
    public TumblrPattern(String expression) { this.expression = expression; }
    @Override
    public String toString() {
        return "TumblrPattern{" +
                "_id='" + _id + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }
}
