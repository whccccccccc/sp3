package com.atguigu.boot3.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Boot313SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(Boot313SecurityApplication.class, args);
    }

}
