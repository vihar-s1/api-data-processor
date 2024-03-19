package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataProducer.models.ApiMessages.MockApiMessage;
import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MockDataFetcher implements DataFetcher{

    @Autowired
    ApiMessageProducerService producerService;

    @Override
    public void fetchData() {
         List<ApiMessageInterface> data = Arrays.asList(
                new MockApiMessage("a000" + Math.random(), "Hello World"),
                new MockApiMessage("a001" + Math.random(), "Hello World"),
                new MockApiMessage("a002" + Math.random(), "How is everyone"),
                new MockApiMessage("a003" + Math.random(), "Lets Mock data"),
                new MockApiMessage("a004" + Math.random(), "Bye World")
        );

        log.info("Messages Fetched via MockDataFetcher");

        log.info("Sending Messages to Kafka");
        data.forEach(kafkaDataObject -> producerService.sendMessage(kafkaDataObject));
    }
}
