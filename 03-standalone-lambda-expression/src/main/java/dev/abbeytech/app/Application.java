package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        //
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            log.info("Spring Boot application started successfully!");
            log.info("This is a standalone (no-web) Spring Boot application using Java 17");
            log.info("Check the tutorial packages for lambda expression examples");
        };
    }
}
