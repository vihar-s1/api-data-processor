package com.apiDataProcessor.searchService.repositories;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface CentralRepository extends ElasticsearchRepository<GenericChannelPost, String> {

    Page<GenericChannelPost> findAllByApiType(ApiType messageType, Pageable pageable);

    Page<GenericChannelPost> findAllByTagsContainingIgnoreCase(String tag, Pageable pageable);

}
