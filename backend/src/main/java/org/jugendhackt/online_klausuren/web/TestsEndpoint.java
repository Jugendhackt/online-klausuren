package org.jugendhackt.online_klausuren.web;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jugendhackt.online_klausuren.Database;
import org.jugendhackt.online_klausuren.GLOBAL_VARS;
import org.jugendhackt.online_klausuren.tasks.Submission;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "test", urlPatterns = {"api/v1/tests"}, loadOnStartup = 1)
public class TestsEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        Database.API_AUTH_ROLES role = GLOBAL_VARS.database.getRoleForAPIAuthToken(req.getHeader("Authorization").replace("Bearer ", ""));
        if (role != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return (role != Database.API_AUTH_ROLES.TEACHER && f.getName().equalsIgnoreCase("tasks")) || f.getName().equalsIgnoreCase("students");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
            String tests = gsonBuilder.create().toJson(GLOBAL_VARS.tests);
            resp.getWriter().println(tests);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
    }
}
