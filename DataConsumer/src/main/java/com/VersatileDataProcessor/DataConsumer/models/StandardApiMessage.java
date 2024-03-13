package com.VersatileDataProcessor.DataConsumer.models;

import com.VersatileDataProcessor.DataConsumer.models.ApiMessages.ApiMessageInterface;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StandardApiMessage implements Serializable {
    private String Id;
    private ApiMessageInterface data;
}
