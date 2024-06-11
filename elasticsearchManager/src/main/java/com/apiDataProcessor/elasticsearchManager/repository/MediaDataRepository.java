package com.apiDataProcessor.elasticsearchManager.repository;

import com.apiDataProcessor.models.standardMediaData.StandardMediaData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MediaDataRepository extends ElasticsearchRepository<StandardMediaData, String> {
}
