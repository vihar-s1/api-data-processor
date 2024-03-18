package com.VersatileDataProcessor.ElasticsearchWriter.models;

import java.io.Serializable;

public enum MessageType implements Serializable {
    TUMBLR,
    REDDIT,
    WEATHER,
    RANDOM_USER,
    MOCK
}
