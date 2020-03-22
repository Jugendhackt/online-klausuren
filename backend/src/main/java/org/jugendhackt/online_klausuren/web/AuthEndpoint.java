package org.jugendhackt.online_klausuren.web;

import static org.jugendhackt.online_klausuren.Database.API_AUTH_ROLES;

import com.google.gson.JsonObject;
import org.jugendhackt.online_klausuren.GLOBAL_VARS;

import static org.jugendhackt.online_klausuren.Database.API_AUTH_ROLES.*;

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
        resp.addHeader("Access-Control-Allow-Origin", "*");

        BufferedReader reader = req.getReader();
        String line = reader.readLine();
        JsonObject object = GLOBAL_VARS.gson.fromJson(line, JsonObject.class);
        String type = object.get("type").getAsString();

        String name = "";
        API_AUTH_ROLES role = STUDENT;

        if (req.getHeader("Authorization") != null) {
            String[] extra = new String[]{""};
            if (GLOBAL_VARS.database.getRoleForAPIAuthToken(req.getHeader("Authorization").replace("Bearer ", ""), extra) != null) {
                name = extra[0];
            }
        } else {
            // Choose Auth Provider. They cancel the request if it is not allowed and set the name
            switch (System.getenv("OK_AUTH_PROVIDER")) {
                case "demo":
                    // WARNING: Demo Login is unsafe
                    name = object.get("username").getAsString();
                    if (name.startsWith("l")) {
                        role = TEACHER;
                    } else {
                        role = STUDENT;
                    }
                    break;
                default:
                    resp.getWriter().println("{\"error\": \"not implemented\"}");
            }
        }

        if (type.equals("test")) {
            String token = GLOBAL_VARS.database.addTestUser(name, object.get("test").getAsString());
            resp.getWriter().println("{\"token\": \"" + token + "\"}");
        } else if (type.equals("api")) {
            String token = GLOBAL_VARS.database.addAPIUser(name, role);
            resp.getWriter().println("{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers", "*");
    }
}
