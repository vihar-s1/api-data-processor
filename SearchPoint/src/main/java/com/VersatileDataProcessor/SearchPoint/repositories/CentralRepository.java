package com.versatileDataProcessor.searchPoint.repositories;

import com.versatileDataProcessor.searchPoint.models.MessageType;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CentralRepository extends ElasticsearchRepository<StandardMessage, String> {
    List<StandardMessage> findAllByMessageType(MessageType messageType, Pageable pageable);
    List<StandardMessage> findAllByTagsContainingIgnoreCase(String tag, Pageable pageable);
    List<StandardMessage> findAllByNameContaining(String name, Pageable pageable);
}
