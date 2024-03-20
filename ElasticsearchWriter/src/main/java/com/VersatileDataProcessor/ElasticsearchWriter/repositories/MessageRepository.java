package com.VersatileDataProcessor.ElasticsearchWriter.repositories;

import com.VersatileDataProcessor.ElasticsearchWriter.models.processedMessages.MessageInterface;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageRepository extends ElasticsearchRepository<MessageInterface, String> {
}