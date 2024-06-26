package com.apiDataProcessor.producer;

import com.apiDataProcessor.producer.service.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class ProducerApplication {

	private final Set<ApiService> apiServices;

	public ProducerApplication(Set<ApiService> apiServices) {
		this.apiServices = apiServices;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Component
	public class ApiServiceScheduler {

		BlockingQueue<Runnable> workerQueue = new LinkedBlockingQueue<>(10);
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, workerQueue);

		@Scheduled(fixedRate = 60_000) // Run every 1 minute
		public void fetchData() {
			apiServices.forEach(apiService -> {
				if (apiService.isExecutable()) {
					threadPoolExecutor.execute(apiService::fetchData);
				}
			});
		}
	}

}
