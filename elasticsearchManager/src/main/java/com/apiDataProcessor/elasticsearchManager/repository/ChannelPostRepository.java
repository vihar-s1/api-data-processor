package com.apiDataProcessor.elasticsearchManager.repository;

import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ChannelPostRepository extends ElasticsearchRepository<GenericChannelPost, String> {
}
