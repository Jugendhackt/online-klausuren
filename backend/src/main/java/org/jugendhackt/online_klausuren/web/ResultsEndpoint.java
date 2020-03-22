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
import java.util.UUID;

@WebServlet(name = "results", urlPatterns = {"api/v1/results/*"}, loadOnStartup = 1)
public class ResultsEndpoint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.addHeader("Content-Type", "text/csv");
    	resp.addHeader("Content-Disposition", "attachment; filename=\"Klausur.csv\"");

        String test = req.getPathInfo().replace("/", "");
        Map<String, Submission[]> submissions = GLOBAL_VARS.database.getSubmissionsForTest(test);
        if (submissions != null && !submissions.isEmpty()) {
        	
        	resp.getWriter().print("Name;");
        	
        	Submission[] example = submissions.get(submissions.keySet().toArray()[0]);
        	String[] uuid_order = new String[example.length];
        	for(int i=0; i<uuid_order.length; i++)
        	{
        		uuid_order[i] = example[i].task.toString();
        	}

        	for(int i=0; i<uuid_order.length; i++)
        	{
        		resp.getWriter().print( uuid_order[i] + ";");
        	}
        	resp.getWriter().println();
        	
            for (String name : submissions.keySet()) {
                resp.getWriter().print(name + ";");
                String[] values = new String[uuid_order.length];
				for(Submission submission: submissions.get(name)) {
					for (int i=0; i < uuid_order.length; i++) {
						if (submission.task.toString().equals(uuid_order[i])) values[i] = submission.value;
					}
				}
                for (String value : values) {
                	resp.getWriter().write(value + ";");
				}
                resp.getWriter().println(); 
            }
        }
    }
}
