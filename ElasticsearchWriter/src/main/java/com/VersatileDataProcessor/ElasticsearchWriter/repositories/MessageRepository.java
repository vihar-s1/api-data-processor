package com.versatileDataProcessor.elasticsearchWriter.repositories;

import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MessageInterface;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageRepository extends ElasticsearchRepository<MessageInterface, String> {
}
