package com.example.testprojectforagencyamazon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@EnableCaching
@SpringBootApplication
public class TestProjectForAgencyAmazonApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestProjectForAgencyAmazonApplication.class, args);
    }

}
