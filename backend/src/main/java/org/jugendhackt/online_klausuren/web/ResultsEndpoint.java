package org.jugendhackt.online_klausuren.web;

import org.jugendhackt.online_klausuren.GLOBAL_VARS;
import org.jugendhackt.online_klausuren.tasks.Submission;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@WebServlet(name = "results", urlPatterns = {"api/v1/results/*"}, loadOnStartup = 1)
public class ResultsEndpoint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String test = req.getPathInfo().replace("/", "");
        Map<String, Submission[]> submissions = GLOBAL_VARS.database.getSubmissionsForTest(test);
        if (submissions != null) {
            for (String name : submissions.keySet()) {
                resp.getWriter().print(name + ":");
                resp.getWriter().println(Arrays.toString(submissions.get(name)));
            }
        }
    }
}
