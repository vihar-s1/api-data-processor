package com.VersatileDataProcessor.DataProducer.models;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaDataObject implements Serializable {
    private String Id;
    private String mockData;
    private int mockCount;
}
