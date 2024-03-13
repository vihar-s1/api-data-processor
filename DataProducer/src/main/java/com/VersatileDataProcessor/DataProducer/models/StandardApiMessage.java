package com.VersatileDataProcessor.DataProducer.models;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StandardApiMessage implements Serializable {
    private String Id;
    private ApiMessageInterface data;
}
