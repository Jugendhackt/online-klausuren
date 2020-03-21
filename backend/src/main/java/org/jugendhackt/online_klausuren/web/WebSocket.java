package org.jugendhackt.online_klausuren.web;

import com.google.gson.JsonObject;
import org.jugendhackt.online_klausuren.GLOBAL_VARS;
import org.jugendhackt.online_klausuren.Test;
import org.jugendhackt.online_klausuren.tasks.Submission;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint("/api/v1/ws")
public class WebSocket {

    @OnOpen
    public void onOpen(Session session){
        // TODO Do Authentication
        if (!GLOBAL_VARS.tests.isEmpty())
            GLOBAL_VARS.tests.get(0).addStudent(session);
    }

    @OnMessage
    public void handleMessage(Session session, String message) {
        WebsocketPacket packet = GLOBAL_VARS.gson.fromJson(message, WebsocketPacket.class);
        if (packet.getFunction().equals("submission")) {
            Test test = GLOBAL_VARS.getTestBySession(session);
            test.submissionSent(session, packet.getData().get("value").getAsString(), packet.getData().get("taskId").getAsString());
        } else {
            // Wrong packet!
        }
    }



    // Helper Method to send Messages
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}