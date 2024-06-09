package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSwagger2
public class FreelancingApplication {
    // Set the default time zone for the JVM to Tunisia
    public static void main(String[] args) {
        SpringApplication.run(FreelancingApplication.class, args);
    }

}
