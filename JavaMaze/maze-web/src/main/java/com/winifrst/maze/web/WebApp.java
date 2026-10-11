package com.winifrst.maze.web;

import com.winifrst.maze.MazeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

    @Bean
    public MazeService mazeService() {
        return new MazeService();
    }
}