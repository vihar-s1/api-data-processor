package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.StandardApiMessage;

import java.util.List;

public interface DataFetcher {
    public List<StandardApiMessage> fetchData();
}
