package com.apiDataProcessor.producer;

import com.apiDataProcessor.producer.service.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Set;

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
}
