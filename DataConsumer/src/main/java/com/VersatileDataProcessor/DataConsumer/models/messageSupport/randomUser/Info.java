package com.VersatileDataProcessor.DataConsumer.models.messageSupport.randomUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Info {
    private String seed, version;
    private int results, page;
}
