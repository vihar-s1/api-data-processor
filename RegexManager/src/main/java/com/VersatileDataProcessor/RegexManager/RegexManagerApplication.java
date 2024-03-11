package com.VersatileDataProcessor.RegexManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class RegexManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RegexManagerApplication.class, args);
	}

}
