package com.versatileDataProcessor.elasticsearchWriter.repositories;

import com.versatileDataProcessor.elasticsearchWriter.models.standardMessage.StandardMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CentralRepository extends ElasticsearchRepository<StandardMessage, String> {
}
