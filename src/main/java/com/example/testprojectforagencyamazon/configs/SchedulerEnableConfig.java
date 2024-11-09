package com.example.testprojectforagencyamazon.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class to enable scheduling tasks in Spring.
 * The @EnableScheduling annotation allows the use of @Scheduled
 * annotations to schedule tasks within the application.
 */
@Configuration
@EnableScheduling
public class SchedulerEnableConfig {

}
