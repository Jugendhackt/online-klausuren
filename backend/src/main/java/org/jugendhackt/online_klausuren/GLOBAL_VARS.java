package org.jugendhackt.online_klausuren;

import com.google.gson.Gson;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GLOBAL_VARS {

    public final static Database database = new Database();
    public final static Gson gson = new Gson();
    public final static List<Test> tests = new ArrayList<>();

    public static Test getTestBySession(Session session) {
        for (Test test : tests) if (test.getStudentBySession(session) != null) return test;
        return null;
    }

    public static Test getTestByUUID(UUID uuid) {
        for (Test test : tests) if (test.getUuid() == uuid) return test;
        return null;
    }

}
