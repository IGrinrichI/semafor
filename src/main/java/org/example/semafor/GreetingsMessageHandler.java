package org.example.semafor;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class GreetingsMessageHandler extends TextWebSocketHandler {

    private List<WebSocketSession> establishedSessions = new CopyOnWriteArrayList<>();
    private HashMap<WebSocketSession,String> resources = new HashMap<>();

    // Вызывается после установки соединения. Добавляет клиента в общий список.
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        establishedSessions.add(session);
    }

    // Вызывается после прерывания соединения. Удаляет клиента из списка.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        establishedSessions.remove(session);
        resources.remove(session);
    }

    // Вызывается после получения сообщения. Рассылает его всем подключенным клиентам.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if(resources.containsValue(message.getPayload())){
            sendMessageToClient(new TextMessage("Failed to extract " + message.getPayload()),session);
        }
        else{
            resources.put(session, message.getPayload());
            sendMessageToClient(new TextMessage("Extract " + message.getPayload()),session);
        }
        /*forEach(establishedSession -> {
            if (!establishedSession.equals(session)) {
                sendMessageToClient(message, establishedSession);
            }
        });*/
    }

    private void sendMessageToClient(TextMessage message,
                                     WebSocketSession establishedSession) {
        try {
            establishedSession.sendMessage(new TextMessage(message.getPayload()));
        } catch (IOException e) {
            System.out.println("Failed to send message." + e);
        }
    }
}