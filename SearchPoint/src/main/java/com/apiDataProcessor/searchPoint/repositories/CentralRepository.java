package com.apiDataProcessor.searchPoint.repositories;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.standardMediaData.StandardMediaData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface CentralRepository extends ElasticsearchRepository<StandardMediaData, String> {

    String findAllByAdditionalDataQueryString = "{\"bool\": {\"must\": [{\"wildcard\": {\"additional.?0\": \"*?1*\"}}]}}";
    String findAllByNameQueryString = "{\"bool\":{\"should\":[{\"wildcard\":{\"name.title\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.first\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.last\":{\"value\":\"*?0*\"}}}]}}";

    List<StandardMediaData> findAllByApiType(ApiType messageType, Pageable pageable);

    // Joke AP
    List<StandardMediaData> findAllByTagsContainingIgnoreCase(String tag, Pageable pageable);

    // Random User API
}
