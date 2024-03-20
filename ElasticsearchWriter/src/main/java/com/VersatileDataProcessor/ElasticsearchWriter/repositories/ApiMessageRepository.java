package com.VersatileDataProcessor.ElasticsearchWriter.repositories;

import com.VersatileDataProcessor.ElasticsearchWriter.models.apiMessages.ApiMessageInterface;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApiMessageRepository extends ElasticsearchRepository<ApiMessageInterface, String> {
}