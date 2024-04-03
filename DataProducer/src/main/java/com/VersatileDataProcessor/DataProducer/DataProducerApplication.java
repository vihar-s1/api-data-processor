package com.versatileDataProcessor.dataProducer;

import com.versatileDataProcessor.dataProducer.fetcher.DataFetcherInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class DataProducerApplication {

	private final Set<DataFetcherInterface> dataFetchers;

	public DataProducerApplication(Set<DataFetcherInterface> dataFetchers) {
		this.dataFetchers = dataFetchers;
	}

	public static void main(String[] args) {
		SpringApplication.run(DataProducerApplication.class, args);
	}

	@Component
	public class DataFetcherScheduler {
		@Scheduled(fixedRate = 60_000) // Run every 1 minute
		public void fetchData() {
			dataFetchers.forEach(DataFetcherInterface::fetchData);
		}
	}

}
