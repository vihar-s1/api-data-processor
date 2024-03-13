package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.MockApiMessage;
import com.VersatileDataProcessor.DataProducer.models.StandardApiMessage;

import java.util.Arrays;
import java.util.List;

public class MockDataFetcher implements DataFetcher{
    @Override
    public List<StandardApiMessage> fetchData() {
        return Arrays.asList(
                new MockApiMessage("a223", "Hello World").toStandardApiMessage(),
                new MockApiMessage("a223", "Hello World").toStandardApiMessage(),
                new MockApiMessage("a224", "How is everyone").toStandardApiMessage(),
                new MockApiMessage("a225", "Lets Mock data").toStandardApiMessage(),
                new MockApiMessage("a229", "Bye World").toStandardApiMessage()
        );
    }
}
