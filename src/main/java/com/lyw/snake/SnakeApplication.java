package com.lyw.snake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnakeApplication.class, args);
        GameLoop.start();
    }
}
