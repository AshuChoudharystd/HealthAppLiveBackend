package org.example.healthappbackendjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HealthAppBackendJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthAppBackendJavaApplication.class, args);
    }

}
