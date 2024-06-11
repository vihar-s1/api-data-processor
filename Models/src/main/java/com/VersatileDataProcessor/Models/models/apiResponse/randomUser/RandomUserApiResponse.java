package com.VersatileDataProcessor.Models.models.apiResponse.randomUser;

import com.VersatileDataProcessor.Models.MessageType;
import com.VersatileDataProcessor.Models.models.apiResponse.ApiResponseInterface;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class RandomUserApiResponse implements ApiResponseInterface {
    private String id = UUID.randomUUID().toString();
    @Getter
    private MessageType messageType = MessageType.RANDOM_USER;

    private List<RandomUser> results;
}
