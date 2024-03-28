package com.VersatileDataProcessor.DataProducer;

import com.VersatileDataProcessor.DataProducer.fetcher.DataFetcherInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private Set<DataFetcherInterface> dataFetchers;

	public static void main(String[] args) {
		SpringApplication.run(DataProducerApplication.class, args);
	}

	@Component
	public class DataFetcherScheduler {
		@Scheduled(fixedRate = 60_000) // Run every 1 minute
		public void fetchData() {
			dataFetchers.forEach(DataFetcherInterface::fetchData);

//			For Testing Newly Added Api-Data-Fetchers
//			dataFetchers.forEach(dataFetcher -> {
//                if (dataFetcher.getClass().getSimpleName().equals("RandomUserApiFetcher")) {
//                    dataFetcher.fetchData();
//                }
//			});
		}
	}

}
