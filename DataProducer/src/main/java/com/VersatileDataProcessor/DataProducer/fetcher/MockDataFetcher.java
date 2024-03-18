package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataProducer.models.ApiMessages.MockApiMessage;

import java.util.Arrays;
import java.util.List;

public class MockDataFetcher implements DataFetcher{
    @Override
    public List<ApiMessageInterface> fetchData() {
        return Arrays.asList(
                new MockApiMessage("a000" + Math.random(), "Hello World"),
                new MockApiMessage("a001" + Math.random(), "Hello World"),
                new MockApiMessage("a002" + Math.random(), "How is everyone"),
                new MockApiMessage("a003" + Math.random(), "Lets Mock data"),
                new MockApiMessage("a004" + Math.random(), "Bye World")
        );
    }
}
