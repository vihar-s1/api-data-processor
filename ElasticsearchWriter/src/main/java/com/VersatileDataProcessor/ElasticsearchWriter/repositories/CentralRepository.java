package com.VersatileDataProcessor.ElasticsearchWriter.repositories;

import com.VersatileDataProcessor.ElasticsearchWriter.models.standardMessage.StandardMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CentralRepository extends ElasticsearchRepository<StandardMessage, String> {
}
