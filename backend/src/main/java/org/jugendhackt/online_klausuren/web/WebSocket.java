package org.jugendhackt.online_klausuren.web;

import com.google.gson.JsonObject;
import org.jugendhackt.online_klausuren.GLOBAL_VARS;
import org.jugendhackt.online_klausuren.Test;
import org.jugendhackt.online_klausuren.tasks.Submission;

import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint("/api/v1/ws")
public class WebSocket {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        if (session.getRequestParameterMap().containsKey("token")) {
            String[] options = new String[]{""};
            Test test = GLOBAL_VARS.database.getTestForAuthToken(session.getRequestParameterMap().get("token").get(0), options);
            if (test != null) {
                test.addStudent(session, options[0]);
            } else {
                session.close();
            }
        }

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