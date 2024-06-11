package com.VersatileDataProcessor.SearchPoint.repositories;

import com.VersatileDataProcessor.SearchPoint.models.MessageType;
import com.VersatileDataProcessor.SearchPoint.models.StandardMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface CentralRepository extends ElasticsearchRepository<StandardMessage, String> {

    String findAllByAdditionalDataQueryString = "{\"bool\": {\"must\": [{\"wildcard\": {\"additionalData.?0\": \"*?1*\"}}]}}";
    String findAllByNameQueryString = "{\"bool\":{\"should\":[{\"wildcard\":{\"name.title\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.first\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.last\":{\"value\":\"*?0*\"}}}]}}";


    List<StandardMessage> findAllByMessageType(MessageType messageType, Pageable pageable);

    @Query(findAllByAdditionalDataQueryString)
    List<StandardMessage> findAllByAdditionalDataContainsIgnoreCase(String key, String value, Pageable pageable);
    // Joke API

    List<StandardMessage> findAllByTagsContainingIgnoreCase(String tag, Pageable pageable);

    // Random User API
    @Query(findAllByNameQueryString)
    List<StandardMessage> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
