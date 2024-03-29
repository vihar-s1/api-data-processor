package com.versatileDataProcessor.dataProducer.fetcher;

import com.versatileDataProcessor.dataProducer.models.apiMessages.ApiMessageInterface;
import com.versatileDataProcessor.dataProducer.models.apiMessages.MockApiMessage;
import com.versatileDataProcessor.dataProducer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MockDataFetcher implements DataFetcherInterface {

    @Autowired
    ApiMessageProducerService producerService;

    @Override
    public void fetchData() {
         List<ApiMessageInterface> data = Arrays.asList(
                 new MockApiMessage("a000" + Math.random(), "Hello World"),
                 new MockApiMessage("a001" + Math.random(), "Good Morning"),
                 new MockApiMessage("a002" + Math.random(), "lorem ipsum dolor set.")
        );

        log.info("Messages Fetched via MockDataFetcher");

        log.info("Sending Messages to Kafka");
        data.forEach(kafkaDataObject -> producerService.sendMessage(kafkaDataObject));
    }
}
