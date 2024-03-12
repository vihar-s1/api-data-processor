package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.KafkaDataObject;

import java.util.List;

public interface DataFetcher {
    public List<KafkaDataObject> fetchData();
}
