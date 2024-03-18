package com.VersatileDataProcessor.DataProducer;

import com.VersatileDataProcessor.DataProducer.fetcher.MockDataFetcher;
import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootApplication
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
		System.out.println("----------Fetching Messages----------");
		List<ApiMessageInterface> data = new MockDataFetcher().fetchData();
		System.out.println("----------Messages Fetched----------");

		System.out.println("----------Sending Messages to Kafka----------");
		data.forEach(kafkaDataObject -> {
			producerService.sendMessage(kafkaDataObject);
		});
		System.out.println("----------Sent Messages to Kafka----------");
    }
}
