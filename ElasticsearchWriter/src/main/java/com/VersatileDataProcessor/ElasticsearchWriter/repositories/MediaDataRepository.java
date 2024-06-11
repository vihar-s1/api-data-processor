package com.VersatileDataProcessor.ElasticsearchWriter.repositories;

import com.VersatileDataProcessor.Models.standardMediaData.StandardMediaData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MediaDataRepository extends ElasticsearchRepository<StandardMediaData, String> {
}
