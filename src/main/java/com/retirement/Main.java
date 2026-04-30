package com.retirement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point.
 * 
 * @SpringBootApplication combines:
 *   - @Configuration: marks this as a config class
 *   - @EnableAutoConfiguration: auto-configures Spring based on dependencies
 *   - @ComponentScan: scans this package and sub-packages for @Component/@Service/@Controller
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
