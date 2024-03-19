package com.VersatileDataProcessor.DataProducer;

import com.VersatileDataProcessor.DataProducer.fetcher.MockDataFetcher;
import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootApplication
@Slf4j
public class DataProducerApplication implements CommandLineRunner {

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@Autowired
	private ApiMessageProducerService producerService;

	public static void main(String[] args) {
		SpringApplication.run(DataProducerApplication.class, args);
	}

	@Override
	public void run(String... args) {

		List<ApiMessageInterface> data = new MockDataFetcher().fetchData();
		log.info("Messages Fetched via DataFetcher");

		log.info("Sending Messages to Kafka");
		data.forEach(kafkaDataObject -> producerService.sendMessage(kafkaDataObject));
		log.info("Sent Messages to Kafka");
    }
}
