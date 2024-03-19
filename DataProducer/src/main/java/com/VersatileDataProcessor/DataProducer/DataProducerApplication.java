package com.VersatileDataProcessor.DataProducer;

import com.VersatileDataProcessor.DataProducer.fetcher.DataFetcher;
import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class DataProducerApplication {

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@Autowired
	private ApiMessageProducerService producerService;
	@Autowired
	private Set<DataFetcher> dataFetchers;

	public static void main(String[] args) {
		SpringApplication.run(DataProducerApplication.class, args);
	}

	@Component
	public class DataFetcherScheduler {
		@Scheduled(fixedRate = 30_000) // Run every 0.5 minute
		public void fetchData() {
			dataFetchers.forEach(DataFetcher::fetchData);
		}
	}

}
