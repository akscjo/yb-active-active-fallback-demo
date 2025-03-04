package com.yugabyte.ybactiveactivefallbackdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class YbActiveActiveFallbackDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(YbActiveActiveFallbackDemoApplication.class, args);
    }
}
