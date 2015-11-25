package com.agoda;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@SpringBootApplication
@PropertySource("classpath:ratelimit.properties")
public class RateLimit {

    public static void main(String[] args) {
        SpringApplication.run(RateLimit.class, args);
    }
}
