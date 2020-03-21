package org.jugendhackt.online_klausuren;

import com.google.gson.annotations.SerializedName;
import org.jugendhackt.online_klausuren.tasks.Submission;
import org.jugendhackt.online_klausuren.tasks.Task;

import java.io.PrintWriter;
import java.io.FileWriter;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private transient Session session;
    private List<Submission> submissions;
    private transient Task currentTask;
    private transient Task[] taskList;

    public Student(Session session, Task[] taskList) {
        this.session = session;
        this.submissions = new ArrayList<>();
        this.taskList = taskList;
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
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Students_data.txt")));
            pWriter.println(GLOBAL_VARS.gson.toJson(submission));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null){
                pWriter.flush();
                pWriter.close();
            } 
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public Session getSession() {
        return session;
    }
}
