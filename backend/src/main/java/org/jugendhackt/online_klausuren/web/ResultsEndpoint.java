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
        String test = req.getPathInfo().replace("/", "");
        Map<String, Submission[]> submissions = GLOBAL_VARS.database.getSubmissionsForTest(test);
        if (submissions != null && !submissions.isEmpty()) {
        	
        	resp.getWriter().print(";");
        	
        	Submission[] example = submissions.get(submissions.keySet().toArray()[0]);
        	UUID[] uuid_order = new UUID[example.length];
        	for(int i=0; i<uuid_order.length; i++)
        	{
        		uuid_order[i] = example[i].task;
        	}
        	
        	for(int i=0; i<submissions.size(); i++)
        	{
        		resp.getWriter().print( uuid_order[i] + ";");
        	}
        	resp.getWriter().println();
        	
            for (String name : submissions.keySet()) {
                resp.getWriter().print(name + ";");
                for(int i=0; i<submissions.size(); i++)
                {
	                for(Submission submission: submissions.get(name))
		        	{
	                	if(submission.task == uuid_order[i])
	                	{
	                		resp.getWriter().print(submission.value);
	                		break;
	                	}
		        	}
                }
                resp.getWriter().println(); 
            }
        }
    }
}
