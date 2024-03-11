package com.VersatileDataProcessor.RegexManager;

import com.VersatileDataProcessor.RegexManager.repository.TumblrPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class RegexManagerApplication {

	@Autowired
	TumblrPatternRepository tumblrPatternRepository;
	public static void main(String[] args) {
		SpringApplication.run(RegexManagerApplication.class, args);
	}

}
