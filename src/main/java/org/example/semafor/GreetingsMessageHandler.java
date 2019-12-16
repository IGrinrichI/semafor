package org.example.semafor;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Controller
public class GreetingsMessageHandler{
    //Список клиентов. Можно использовать для рассылки сообщений внутри одного сервера.
    //private List<WebSocketSession> establishedSessions = new CopyOnWriteArrayList<>();

    //Хранилище занятых ресурсов (Клиент, Ресурс).
    private Map<String,String> resources;

    //Конструктор, подсасывает мапу из хазелкаст инстанса.
    @Autowired
    GreetingsMessageHandler(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance){
        this.resources = hazelcastInstance.getMap("resources");
    }

    //Веб сокет. Служит для обмена информацией с клиентом.
    @Bean
    WebSocketConfigurer webSocketConfigurer(){
        return new WebSocketConfigurer() {
            @Override
            public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                registry.addHandler(new TextWebSocketHandler(){
                    // Вызывается после установки соединения. Добавляет клиента в общий список.
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session){
                        //establishedSessions.add(session);
                    }

                    // Вызывается после прерывания соединения. Удаляет клиента из списка.
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                        //establishedSessions.remove(session);
                        resources.remove(session.getId());
                    }

                    // Вызывается после получения сообщения. Отправляет сообщение обратно клиенту.
                    @Override
                    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
                        //session.sendMessage(new TextMessage("Extract " + message.getPayload()));
                        if(resources.containsValue(message.getPayload())){
                            session.sendMessage(new TextMessage("Failed to extract " + message.getPayload()));
                        }
                        else{
                            resources.put(session.getId(), message.getPayload());
                            session.sendMessage(new TextMessage("Extract " + message.getPayload()));
                        }
                    }}, "/greetings");
            }
        };
    }




}