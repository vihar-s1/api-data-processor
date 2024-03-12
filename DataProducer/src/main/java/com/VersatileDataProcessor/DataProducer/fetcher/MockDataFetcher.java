package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.KafkaDataObject;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

public class MockDataFetcher implements DataFetcher{
    @Override
    public List<KafkaDataObject> fetchData() {
        return Arrays.asList(
                new KafkaDataObject("a223", "Hello World", 3),
                new KafkaDataObject("a224", "How is everyone", 4),
                new KafkaDataObject("a225", "Lets Mock data", 2),
                new KafkaDataObject("a229", "Bye Workd", 7)
        );
    }
}
