package com.versatileDataProcessor.searchPoint.repositories;

import com.versatileDataProcessor.searchPoint.models.MessageType;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CentralRepository extends ElasticsearchRepository<StandardMessage, String> {
    public List<StandardMessage> findAllByMessageType(MessageType messageType, Pageable pageable);
}
