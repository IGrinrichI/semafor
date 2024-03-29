package org.example.semafor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class Application {

    public static void main(String[]args) {
        SpringApplication app = new SpringApplication(Application.class);
        //app.setDefaultProperties(Collections
        //        .singletonMap("server.port", "8080"));
        app.run(args);
    }

}