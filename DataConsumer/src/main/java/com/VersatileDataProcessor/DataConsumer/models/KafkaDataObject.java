package com.VersatileDataProcessor.DataConsumer.models;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaDataObject implements Serializable {
    private String Id;
    private String mockData;
    private int mockCount;
}
