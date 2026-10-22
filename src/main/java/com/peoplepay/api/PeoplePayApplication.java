package com.peoplepay.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.peoplepay")
public class PeoplePayApplication {
    public static void main(String[] args) { SpringApplication.run(PeoplePayApplication.class, args); }
}
