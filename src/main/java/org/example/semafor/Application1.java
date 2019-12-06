package org.example.semafor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Application1 {

    public static void main(String[]args) {
        SpringApplication app = new SpringApplication(Application1.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8081"));
        app.run(args);
    }

}