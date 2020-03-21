package org.jugendhackt.online_klausuren;

import com.google.gson.annotations.SerializedName;
import org.jugendhackt.online_klausuren.tasks.Submission;
import org.jugendhackt.online_klausuren.tasks.Task;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private transient Session session;
    private List<Submission> submissions;
    private final String name;
    private transient Task currentTask;
    private transient Task[] taskList;

    public Student(Session session, String name, Task[] taskList) {
        this.session = session;
        this.submissions = new ArrayList<>();
        this.taskList = taskList;
        this.name = name;
    }

    public Task[] getTaskList() {
        return taskList;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        
        PrintWriter pWriter = null;
        try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Students_data.txt", true)));
            pWriter.println(GLOBAL_VARS.gson.toJson(submission) + session.getId());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null){
                pWriter.flush();
                pWriter.close();
            }
        } 
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public Session getSession() {
        return session;
    }
}
