package org.jugendhackt.online_klausuren.web;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/api/v1/ws")
public class WebSocket {

    @OnOpen
    public void onOpen(Session session){

    }

    @OnMessage
    public void handleMessage(Session session, String message) {
        sendMessage(session, "Hello World!");
    }


    // Helper Method to send Messages
    private static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText("Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}