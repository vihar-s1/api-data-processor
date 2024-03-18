package com.VersatileDataProcessor.ElasticsearchWriter.repositories;

import com.VersatileDataProcessor.ElasticsearchWriter.models.ApiMessages.ApiMessageInterface;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApiMessageRepository extends ElasticsearchRepository<ApiMessageInterface, String> {
}