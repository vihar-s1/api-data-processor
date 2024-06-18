package com.apiDataProcessor.searchService.repositories;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface CentralRepository extends ElasticsearchRepository<GenericChannelPost, String> {

    String findAllByAdditionalDataQueryString = "{\"bool\": {\"must\": [{\"wildcard\": {\"additional.?0\": \"*?1*\"}}]}}";
    String findAllByNameQueryString = "{\"bool\":{\"should\":[{\"wildcard\":{\"name.title\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.first\":{\"value\":\"*?0*\"}}},{\"wildcard\":{\"name.last\":{\"value\":\"*?0*\"}}}]}}";

    List<GenericChannelPost> findAllByApiType(ApiType messageType, Pageable pageable);

    // Joke AP
    List<GenericChannelPost> findAllByTagsContainingIgnoreCase(String tag, Pageable pageable);

    // Random User API
}
