package org.jugendhackt.online_klausuren.web;

import com.google.gson.JsonObject;
import org.jugendhackt.online_klausuren.GLOBAL_VARS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "auth", urlPatterns = {"api/v1/auth"}, loadOnStartup = 1)
public class AuthEndpoint extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        String line = reader.readLine();
        JsonObject object = GLOBAL_VARS.gson.fromJson(line, JsonObject.class);
        switch (object.get("type").getAsString()) {
            case "demo":
                // Demo Login is unsafe and needs to be allowed specifically
                if (System.getenv("OK_ALLOW_DEMO_LOGIN").equals("TRUE")) {
                    resp.getWriter().println("{\"token\": \"" + GLOBAL_VARS.database.addUser(object.get("username").getAsString(), object.get("test").getAsString()) + "\"}");
                }
                break;
            default:
                resp.getWriter().println("{\"error\": \"not implemented, yet\"}");
        }
    }
}
