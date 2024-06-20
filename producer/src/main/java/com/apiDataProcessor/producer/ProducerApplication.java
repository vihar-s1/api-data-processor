package com.apiDataProcessor.producer;

import com.apiDataProcessor.producer.handler.ApiDataHandlerInterface;
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
public class ProducerApplication {

	private final Set<ApiDataHandlerInterface> apiDataHandlers;

	public ProducerApplication(Set<ApiDataHandlerInterface> apiDataHandlers) {
		this.apiDataHandlers = apiDataHandlers;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Component
	public class DataFetcherScheduler {
		@Scheduled(fixedRate = 60_000) // Run every 1 minute
		public void fetchData() {
			apiDataHandlers.forEach(ApiDataHandlerInterface::fetchData);
		}
	}

}
