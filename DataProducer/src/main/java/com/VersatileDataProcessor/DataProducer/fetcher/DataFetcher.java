package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;

import java.util.List;

public interface DataFetcher {
    List<ApiMessageInterface> fetchData();
}
