package org.jugendhackt.online_klausuren;

import com.google.gson.annotations.SerializedName;
import org.jugendhackt.online_klausuren.tasks.Submission;
import org.jugendhackt.online_klausuren.tasks.Task;

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
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public Session getSession() {
        return session;
    }
}
